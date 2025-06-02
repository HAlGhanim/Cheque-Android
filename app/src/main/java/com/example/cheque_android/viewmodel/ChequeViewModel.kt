package com.example.cheque_android.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheque_android.data.dto.*
import com.example.cheque_android.data.request.*
import com.example.cheque_android.data.response.*
import com.example.cheque_android.network.ChequeApiService
import com.example.cheque_android.network.RetrofitHelper
import com.example.cheque_android.utils.TokenManager
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ChequeViewModel(
    private val context: Context,
    private val apiService: ChequeApiService = RetrofitHelper.getInstance(context).create(ChequeApiService::class.java)
) : ViewModel() {

    var token: TokenResponse? by mutableStateOf(null)
        private set

    var chequeAccount: Account? by mutableStateOf(null)
        private set

    var transactions: List<TransactionResponse> by mutableStateOf(emptyList())
        private set

    var errorMessage: String? by mutableStateOf(null)
        internal set

    var user: User? by mutableStateOf(null)
        private set

    var name by mutableStateOf("")
    var kycName by mutableStateOf("")

    var dashboardStats: DashboardStats? by mutableStateOf(null)
        private set

    var users: List<User> by mutableStateOf(emptyList())
        private set

    var chequeAccounts: List<Account> by mutableStateOf(emptyList())
        private set

    var transfers: List<TransferResponse> by mutableStateOf(emptyList())
        private set

    var paymentLinks: List<PaymentLinkResponse> by mutableStateOf(emptyList())
        private set

    var kycRecords: List<KYC> by mutableStateOf(emptyList())
        private set

    var activeCodeCount: Int? by mutableStateOf(null)
        private set

    var generatedCode: Map<String, Any>? by mutableStateOf(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadStoredToken()
    }

    private var hasInitialized = false

    fun loadStoredToken() {
        val savedToken = TokenManager.getToken(context)
        token = savedToken?.let { TokenResponse(it) }

        if (!hasInitialized && savedToken != null) {
            hasInitialized = true
            fetchCurrentUser()
        }
    }

    fun clearErrorMessage() {
        errorMessage = null
    }

    fun logout() {
        TokenManager.clearToken(context)
        resetState()
        Log.d("Logout", "Logged out and state reset")
    }

    private fun resetState() {
        token = null
        chequeAccount = null
        transactions = emptyList()
        user = null
        dashboardStats = null
        users = emptyList()
        chequeAccounts = emptyList()
        transfers = emptyList()
        paymentLinks = emptyList()
        kycRecords = emptyList()
        activeCodeCount = null
        generatedCode = null
        errorMessage = null
    }

    fun login(username: String, password: String, onNavigate: ((String) -> Unit)? = null) {
        viewModelScope.launch {
            isLoading = true
            try {
                val authResponse = apiService.login(User(email = username, password = password)).body()
                token = authResponse
                val rawToken = authResponse?.token
                if (rawToken.isNullOrBlank()) {
                    errorMessage = "Token is null in response"
                    onNavigate?.invoke("home")
                    isLoading = false
                    return@launch
                }

                TokenManager.saveToken(context, rawToken)
                this@ChequeViewModel.token = TokenResponse(rawToken)

                try {
                    val fetchedUser = apiService.getCurrentUser().body()
                    user = fetchedUser
                    val route = if (fetchedUser?.role == Role.ADMIN) "admin_dashboard" else "home"
                    onNavigate?.invoke(route)
                    getMyAccount()
                } catch (e: Exception) {
                    errorMessage = "Failed to fetch user: ${e.message}"
                }
            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }


    fun registerFullFlow(
        name: String,
        phone: String,
        email: String,
        password: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val registerResponse = apiService.register(RegisterRequest(email, password)).body()
                val tokenValue = registerResponse?.token
                if (tokenValue.isNullOrBlank()) {
                    onError("Token missing after registration")
                    return@launch
                }
                TokenManager.saveToken(context, tokenValue)
                token = TokenResponse(tokenValue)

                try {
                    apiService.createAccount(AccountRequest(role.uppercase()))
                    try {
                        apiService.createKyc(KYCRequest(name, phone))
                        onSuccess()
                    } catch (e: Exception) {
                        onError("Failed to create KYC: ${e.message}")
                    }
                } catch (e: Exception) {
                    onError("Failed to create account: ${e.message}")
                }
            } catch (e: Exception) {
                onError("Registration failed: ${e.message}")
            }
        }
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentUser()
                if (response.isSuccessful) {
                    user = response.body()
                } else {
                    errorMessage = "Failed to fetch user"
                }
            } catch (e: Exception) {
                errorMessage = "Exception: ${e.localizedMessage}"
            }
        }
    }

    fun getMyAccount() {
        viewModelScope.launch {
            try {
                val response = apiService.getMyAccount()
                if (response.isSuccessful) {
                    chequeAccount = response.body()?.firstOrNull()
                    if (chequeAccount != null) {
                        getMyTransactions()
                    }
                }
            } catch (e: Exception) {
                Log.e("GetMyAccount", "Failed to fetch account: ${e.message}")
            }
        }
    }

    fun getMyTransactions() {
        viewModelScope.launch {
            try {
                val response = apiService.getMyTransactions()
                if (response.isSuccessful) {
                    transactions = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("GetTransactions", "Failed to fetch transactions: ${e.message}")
            }
        }
    }

    fun fetchDashboardStats() {
        viewModelScope.launch {
            try {
                val response = apiService.getDashboardStats()
                if (response.isSuccessful) {
                    dashboardStats = response.body()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchUsers(page: Int = 1, size: Int = 10, role: String? = null) {
        viewModelScope.launch {
            try {
                val response = apiService.getUsers(page, size, role)
                if (response.isSuccessful) {
                    users = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            try {
                apiService.deleteUser(userId)
                fetchUsers()
                errorMessage = "User deleted successfully"
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun suspendUser(userId: Long) {
        viewModelScope.launch {
            try {
                apiService.suspendUser(userId)
                fetchUsers()
                errorMessage = "User suspended successfully"
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchAccounts() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllAccounts()
                if (response.isSuccessful) {
                    chequeAccounts = response.body()!!
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTransactions()
                if (response.isSuccessful) {
                    transactions = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchTransfers() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTransfers()
                if (response.isSuccessful) {
                    transfers = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchPaymentLinks() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllPaymentLinks()
                if (response.isSuccessful) {
                    paymentLinks = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun deletePaymentLink(linkId: Long) {
        viewModelScope.launch {
            try {
                apiService.deletePaymentLink(linkId)
                fetchPaymentLinks()
                errorMessage = "Payment link deleted successfully"
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchKYC() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllKYC()
                if (response.isSuccessful) {
                    kycRecords = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchActiveCodeCount() {
        viewModelScope.launch {
            try {
                val response = apiService.getActiveCodeCount()
                if (response.isSuccessful) {
                    activeCodeCount = (response.body() as? Map<*, *>)?.get("activeCodes") as? Int
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun generateRedeemCode(amount: BigDecimal) {
        viewModelScope.launch {
            try {
                val response = apiService.generateRedeemCode(RedeemRequest(amount))
                if (response.isSuccessful) {
                    generatedCode = response.body()
                    fetchActiveCodeCount()
                    errorMessage = "Redeem code generated successfully"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun loadKycData() {
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentUser()
                if (response.isSuccessful) {
                    kycName = response.body()?.email ?: "User"
                }
            } catch (e: Exception) {
                Log.e("KycData", "Failed to load KYC data: ${e.message}")
            }
        }
    }

    fun redeemCode(code: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.redeemCode(code)
                if (response.isSuccessful) {
                    val message = response.body()?.get("message") ?: "Redeemed successfully"
                    getMyAccount()
                    getMyTransactions()
                    onResult(message)
                } else {
                    val error = response.errorBody()?.string() ?: "Failed to redeem"
                    onResult("Error: $error")
                }
            } catch (e: Exception) {
                onResult("Exception: ${e.localizedMessage}")
            }
        }
    }

    fun getTransactionsByAccount(accountNumber: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getTransactionsByAccount(accountNumber)
                transactions = response
            } catch (e: Exception) {
                Log.e("ChequeViewModel", "Failed to get transactions: ${e.message}")
            }
        }
    }

}