package com.example.cheque_android.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheque_android.data.dto.AccountResponse
import com.example.cheque_android.data.dto.DashboardStats
import com.example.cheque_android.data.dto.KYC
import com.example.cheque_android.data.dto.Role
import com.example.cheque_android.data.dto.User
import com.example.cheque_android.data.request.AccountRequest
import com.example.cheque_android.data.request.KYCRequest
import com.example.cheque_android.data.request.RedeemRequest
import com.example.cheque_android.data.request.RegisterRequest
import com.example.cheque_android.data.response.PaymentLinkResponse
import com.example.cheque_android.data.response.TokenResponse
import com.example.cheque_android.data.response.TransactionResponse
import com.example.cheque_android.data.response.TransferResponse
import com.example.cheque_android.network.ChequeApiService
import com.example.cheque_android.network.RetrofitHelper
import com.example.cheque_android.utils.TokenManager
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ChequeViewModel(private val context: Context) : ViewModel() {
    private val apiService = RetrofitHelper.getInstance(context).create(ChequeApiService::class.java)
    var token: TokenResponse? by mutableStateOf(null)
    var user: User? by mutableStateOf(null)
    var name by mutableStateOf("")
    var errorMessage: String? by mutableStateOf(null)

    // Admin States
    var dashboardStats: DashboardStats? by mutableStateOf(null)
    var users: List<User> by mutableStateOf(emptyList())
    var accounts: List<AccountResponse> by mutableStateOf(emptyList())
    var transactions: List<TransactionResponse> by mutableStateOf(emptyList())
    var transfers: List<TransferResponse> by mutableStateOf(emptyList())
    var paymentLinks: List<PaymentLinkResponse> by mutableStateOf(emptyList())
    var kycRecords: List<KYC> by mutableStateOf(emptyList())
    var activeCodeCount: Int? by mutableStateOf(null)
    var generatedCode: Map<String, Any>? by mutableStateOf(null)

    init {
        loadStoredToken()
    }

    fun loadStoredToken() {
        Log.d("Token", "Loaded token: ${TokenManager.getToken(context)}")
        token = TokenResponse(TokenManager.getToken(context))
        if (!TokenManager.getToken(context).isNullOrBlank()) {
            fetchCurrentUser()
        }
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentUser()
                if (response.isSuccessful) {
                    user = response.body()
                    Log.d("FetchUser", "User fetched: ${user?.email}, Role: ${user?.role}")
                } else {
                    Log.e("FetchUser", "Failed: ${response.code()}")
                    logout()
                }
            } catch (e: Exception) {
                Log.e("FetchUser", "Error: ${e.message}")
                logout()
            }
        }
    }

    fun login(username: String, password: String, onNavigate: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.login(User(email = username, password = password))
                Log.d("LoginResponse", "Raw response: ${response.raw()}")
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    Log.d("Login", "AuthResponse: $authResponse")
                    token = authResponse
                    token?.token?.let {
                        Log.d("Login", "Extracted token: $it")
                        TokenManager.saveToken(context, it)
                        loadStoredToken()
                        Log.d("Login", "Token saved: $it")
                        val userResponse = apiService.getCurrentUser()
                        if (userResponse.isSuccessful) {
                            user = userResponse.body()
                            Log.d("Login", "User fetched: email=${user?.email}, role=${user?.role}")
                            val route = if (user?.role == Role.ADMIN) "admin_dashboard" else "home"
                            onNavigate(route)
                        } else {
                            Log.e("Login", "Failed to fetch user: ${userResponse.code()}")
                            onNavigate("home")
                        }
                    } ?: run {
                        Log.e("Login", "Token is null in AuthResponse")
                        onNavigate("home")
                    }
                } else {
                    errorMessage = "Login failed: ${response.message()}"
                    Log.e("Login", "Login failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Login error: ${e.message}"
                Log.e("Login", "Error: ${e.message}", e)
            }
        }
    }

    fun logout() {
        TokenManager.clearToken(context)
        token = null
        user = null
        dashboardStats = null
        users = emptyList()
        accounts = emptyList()
        transactions = emptyList()
        transfers = emptyList()
        paymentLinks = emptyList()
        kycRecords = emptyList()
        activeCodeCount = null
        generatedCode = null
        errorMessage = null
        Log.d("Logout", "Token cleared")
    }

    // Admin Functions
    fun fetchDashboardStats() {
        viewModelScope.launch {
            try {
                val response = apiService.getDashboardStats()
                if (response.isSuccessful) {
                    dashboardStats = response.body()
                    errorMessage = null
                    Log.d("Dashboard", "Stats fetched: ${dashboardStats?.totalUsers}")
                } else {
                    errorMessage = "Failed to fetch dashboard stats: ${response.code()}"
                    Log.e("Dashboard", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching dashboard stats: ${e.message}"
                Log.e("Dashboard", "Error: ${e.message}", e)
            }
        }
    }

    fun fetchUsers(page: Int = 1, size: Int = 10, role: String? = null) {
        viewModelScope.launch {
            try {
                val response = apiService.getUsers(page, size, role)
                if (response.isSuccessful) {
                    users = response.body() ?: emptyList()
                    errorMessage = null
                    Log.d("Users", "Users fetched: ${users.size}")
                } else {
                    errorMessage = "Failed to fetch users: ${response.code()}"
                    Log.e("Users", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching users: ${e.message}"
                Log.e("Users", "Error: ${e.message}", e)
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteUser(userId)
                if (response.isSuccessful) {
                    fetchUsers()
                    errorMessage = "User deleted successfully"
                    Log.d("DeleteUser", "User $userId deleted")
                } else {
                    errorMessage = "Failed to delete user: ${response.code()}"
                    Log.e("DeleteUser", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error deleting user: ${e.message}"
                Log.e("DeleteUser", "Error: ${e.message}", e)
            }
        }
    }

    fun suspendUser(userId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.suspendUser(userId)
                if (response.isSuccessful) {
                    fetchUsers()
                    errorMessage = "User suspended successfully"
                    Log.d("SuspendUser", "User $userId suspended")
                } else {
                    errorMessage = "Failed to suspend user: ${response.code()}"
                    Log.e("SuspendUser", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error suspending user: ${e.message}"
                Log.e("SuspendUser", "Error: ${e.message}", e)
            }
        }
    }

    fun fetchAccounts() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllAccounts()
                if (response.isSuccessful) {
                    accounts = response.body() ?: emptyList()
                    errorMessage = null
                    Log.d("Accounts", "Accounts fetched: ${accounts.size}")
                } else {
                    errorMessage = "Failed to fetch accounts: ${response.code()}"
                    Log.e("Accounts", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching accounts: ${e.message}"
                Log.e("Accounts", "Error: ${e.message}", e)
            }
        }
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTransactions()
                if (response.isSuccessful) {
                    transactions = response.body() ?: emptyList()
                    errorMessage = null
                    Log.d("Transactions", "Transactions fetched: ${transactions.size}")
                } else {
                    errorMessage = "Failed to fetch transactions: ${response.code()}"
                    Log.e("Transactions", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching transactions: ${e.message}"
                Log.e("Transactions", "Error: ${e.message}", e)
            }
        }
    }

    fun fetchTransfers() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTransfers()
                if (response.isSuccessful) {
                    transfers = response.body() ?: emptyList()
                    errorMessage = null
                    Log.d("Transfers", "Transfers fetched: ${transfers.size}")
                } else {
                    errorMessage = "Failed to fetch transfers: ${response.code()}"
                    Log.e("Transfers", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching transfers: ${e.message}"
                Log.e("Transfers", "Error: ${e.message}", e)
            }
        }
    }

    fun fetchPaymentLinks() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllPaymentLinks()
                if (response.isSuccessful) {
                    paymentLinks = response.body() ?: emptyList()
                    errorMessage = null
                    Log.d("PaymentLinks", "Payment links fetched: ${paymentLinks.size}")
                } else {
                    errorMessage = "Failed to fetch payment links: ${response.code()}"
                    Log.e("PaymentLinks", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching payment links: ${e.message}"
                Log.e("PaymentLinks", "Error: ${e.message}", e)
            }
        }
    }

    fun deletePaymentLink(linkId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.deletePaymentLink(linkId)
                if (response.isSuccessful) {
                    fetchPaymentLinks()
                    errorMessage = "Payment link deleted successfully"
                    Log.d("DeletePaymentLink", "Payment link $linkId deleted")
                } else {
                    errorMessage = "Failed to delete payment link: ${response.code()}"
                    Log.e("DeletePaymentLink", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error deleting payment link: ${e.message}"
                Log.e("DeletePaymentLink", "Error: ${e.message}", e)
            }
        }
    }

    fun fetchKYC() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllKYC()
                if (response.isSuccessful) {
                    kycRecords = response.body() ?: emptyList()
                    errorMessage = null
                    Log.d("KYC", "KYC records fetched: ${kycRecords.size}")
                } else {
                    errorMessage = "Failed to fetch KYC records: ${response.code()}"
                    Log.e("KYC", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching KYC records: ${e.message}"
                Log.e("KYC", "Error: ${e.message}", e)
            }
        }
    }

    fun fetchActiveCodeCount() {
        viewModelScope.launch {
            try {
                val response = apiService.getActiveCodeCount()
                if (response.isSuccessful) {
                    activeCodeCount = response.body()?.get("activeCodes") as? Int
                    errorMessage = null
                    Log.d("RedeemCount", "Active code count: $activeCodeCount")
                } else {
                    errorMessage = "Failed to fetch active code count: ${response.code()}"
                    Log.e("RedeemCount", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching active code count: ${e.message}"
                Log.e("RedeemCount", "Error: ${e.message}", e)
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
                    Log.d("GenerateCode", "Code generated: $generatedCode")
                } else {
                    errorMessage = "Failed to generate redeem code: ${response.code()}"
                    Log.e("GenerateCode", "Failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Error generating redeem code: ${e.message}"
                Log.e("GenerateCode", "Error: ${e.message}", e)
            }
        }
    }

    fun clearErrorMessage() {
        errorMessage = null
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
                // 1. Register user
                val registerRequest = RegisterRequest(email = email, password = password)
                val registerResponse = apiService.register(registerRequest)

                if (!registerResponse.isSuccessful) {
                    onError("Register failed: ${registerResponse.code()}")
                    return@launch
                }

                val extractedToken = registerResponse.body()?.token
                Log.d("RegisterDebug", "Bearer $extractedToken")

                if (extractedToken.isNullOrBlank()) {
                    onError("Token missing after registration")
                    return@launch
                }

                TokenManager.saveToken(context, extractedToken)

                // 2. Create account
                val accountResponse = apiService.createAccount(
                    accountRequest = AccountRequest(accountType = role.uppercase())
                )

                if (!accountResponse.isSuccessful) {
                    onError("Account creation failed: ${accountResponse.code()}")
                    return@launch
                }

                // 3. Create KYC
                val kycResponse = apiService.createKyc(KYCRequest(name = name, phone = phone))
                if (!kycResponse.isSuccessful) {
                    onError("KYC creation failed: ${kycResponse.code()}")
                    return@launch
                }

                onSuccess()
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }
}