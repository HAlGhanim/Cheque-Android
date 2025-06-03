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
    var totalCodeCount: Int? by mutableStateOf(null)
        private set
    var inactiveCodeCount: Int? by mutableStateOf(null)
        private set
    var allCodes: List<RedeemCodeResponse> by mutableStateOf(emptyList())
    var generatedCode: Map<String, Any>? by mutableStateOf(null)
    private set
    var isLoading by mutableStateOf(false)
        private set
    var accountSearchQuery by mutableStateOf("")
        private set
    var filteredChequeAccounts by mutableStateOf<List<Account>>(emptyList())
        private set
    var kycSearchQuery by mutableStateOf("")
        private set
    var filteredKycRecords by mutableStateOf<List<KYC>>(emptyList())
        private set
    var paymentLinkSearchQuery by mutableStateOf("")
        private set
    var filteredPaymentLinks by mutableStateOf<List<PaymentLinkResponse>>(emptyList())
        private set
    var transactionSearchQuery by mutableStateOf("")
        private set
    var filteredTransactions by mutableStateOf<List<TransactionResponse>>(emptyList())
        private set
    var transferSearchQuery by mutableStateOf("")
        private set
    var filteredTransfers by mutableStateOf<List<TransferResponse>>(emptyList())
        private set
    var userSearchQuery by mutableStateOf("")
        private set
    var filteredUsers by mutableStateOf<List<User>>(emptyList())
        private set

    var isAccountLoaded by mutableStateOf(false)
        private set

    var lastCreatedLink: PaymentLinkResponse? by mutableStateOf(null)
        private set

    var lastUsedLink: PaymentLinkResponse? by mutableStateOf(null)


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
        filteredChequeAccounts = emptyList()
        accountSearchQuery = ""
        transfers = emptyList()
        paymentLinks = emptyList()
        kycRecords = emptyList()
        activeCodeCount = null
        totalCodeCount = null
        inactiveCodeCount = null
        allCodes = emptyList()
        generatedCode = null
        errorMessage = null
    }

    fun updateKycSearchQuery(query: String) {
        kycSearchQuery = query
        updateFilteredKycRecords()
    }

    private fun updateFilteredKycRecords() {
        filteredKycRecords = kycRecords.filter { kyc ->
            kycSearchQuery.isEmpty() ||
                    kyc.id.toString().contains(kycSearchQuery, ignoreCase = true) ||
                    kyc.name.contains(kycSearchQuery, ignoreCase = true) ||
                    kyc.phone.contains(kycSearchQuery, ignoreCase = true) ||
                    kyc.user.email.contains(kycSearchQuery, ignoreCase = true)
        }
    }

    fun updateAccountSearchQuery(query: String) {
        accountSearchQuery = query
        updateFilteredAccounts()
    }

    private fun updateFilteredAccounts() {
        filteredChequeAccounts = chequeAccounts.filter { account ->
            accountSearchQuery.isEmpty() ||
                    account.accountNumber.contains(accountSearchQuery, ignoreCase = true) ||
                    account.userId.toString().contains(accountSearchQuery, ignoreCase = true) ||
                    account.accountType.contains(accountSearchQuery, ignoreCase = true)
        }
        Log.d("ChequeViewModel", "Updated filteredChequeAccounts: $filteredChequeAccounts")
    }

    fun updatePaymentLinkSearchQuery(query: String) {
        paymentLinkSearchQuery = query
        updateFilteredPaymentLinks()
    }

    private fun updateFilteredPaymentLinks() {
        filteredPaymentLinks = paymentLinks.filter { link ->
            paymentLinkSearchQuery.isEmpty() ||
                    link.id.toString().contains(paymentLinkSearchQuery, ignoreCase = true) ||
                    link.accountNumber.contains(paymentLinkSearchQuery, ignoreCase = true) ||
                    link.amount.toString().contains(paymentLinkSearchQuery, ignoreCase = true) ||
                    (link.description?.contains(paymentLinkSearchQuery, ignoreCase = true) ?: false) ||
                    link.status.contains(paymentLinkSearchQuery, ignoreCase = true) ||
                    (link.transactionId?.toString()?.contains(paymentLinkSearchQuery, ignoreCase = true) ?: false) ||
                    link.uuid.contains(paymentLinkSearchQuery, ignoreCase = true)
        }
    }

    fun updateTransactionSearchQuery(query: String) {
        transactionSearchQuery = query
        updateFilteredTransactions()
    }

    private fun updateFilteredTransactions() {
        filteredTransactions = transactions.filter { transaction ->
            transactionSearchQuery.isEmpty() ||
                    transaction.id.toString().contains(transactionSearchQuery, ignoreCase = true) ||
                    transaction.senderAccountNumber.contains(transactionSearchQuery, ignoreCase = true) ||
                    transaction.receiverAccountNumber.contains(transactionSearchQuery, ignoreCase = true) ||
                    transaction.amount.toString().contains(transactionSearchQuery, ignoreCase = true) ||
                    transaction.createdAt.contains(transactionSearchQuery, ignoreCase = true)
        }
    }

    fun updateTransferSearchQuery(query: String) {
        transferSearchQuery = query
        updateFilteredTransfers()
    }

    private fun updateFilteredTransfers() {
        filteredTransfers = transfers.filter { transfer ->
            transferSearchQuery.isEmpty() ||
                    transfer.id.toString().contains(transferSearchQuery, ignoreCase = true) ||
                    transfer.fromUserId.toString().contains(transferSearchQuery, ignoreCase = true) ||
                    transfer.toUserId.toString().contains(transferSearchQuery, ignoreCase = true) ||
                    transfer.senderAccountNumber.contains(transferSearchQuery, ignoreCase = true) ||
                    transfer.receiverAccountNumber.contains(transferSearchQuery, ignoreCase = true) ||
                    transfer.amount.toString().contains(transferSearchQuery, ignoreCase = true) ||
                    transfer.transactionId.toString().contains(transferSearchQuery, ignoreCase = true) ||
                    (transfer.description?.contains(transferSearchQuery, ignoreCase = true) ?: false) ||
                    transfer.createdAt.contains(transferSearchQuery, ignoreCase = true)
        }
    }

    fun updateUserSearchQuery(query: String) {
        userSearchQuery = query
        updateFilteredUsers()
    }

    private fun updateFilteredUsers() {
        filteredUsers = users.filter { user ->
            userSearchQuery.isEmpty() ||
                    (user.id?.toString()?.contains(userSearchQuery, ignoreCase = true) ?: false) ||
                    user.email.contains(userSearchQuery, ignoreCase = true) ||
                    (user.role?.name?.contains(userSearchQuery, ignoreCase = true) ?: false) ||
                    user.status.contains(userSearchQuery, ignoreCase = true)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val authResponse = apiService.login(User(email = username, password = password)).body()
                val rawToken = authResponse?.token

                if (rawToken.isNullOrBlank()) {
                    errorMessage = "Token is null in response"
                    isLoading = false
                    return@launch
                }

                TokenManager.saveToken(context, rawToken)
                token = TokenResponse(rawToken)

                val fetchedUser = apiService.getCurrentUser().body()
                if (fetchedUser == null) {
                    errorMessage = "Failed to fetch user"
                } else {
                    user = fetchedUser
                    getMyAccount()
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
            isAccountLoaded = false
            try {
                val response = apiService.getMyAccount()
                if (response.isSuccessful) {
                    chequeAccount = response.body()?.firstOrNull()
                    chequeAccount?.let {
                        getTransactionsByAccount(it.accountNumber)
                    }
                }
            } catch (e: Exception) {
                Log.e("GetMyAccount", "Failed to fetch account: ${e.message}")
            } finally {
                isAccountLoaded = true
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
                    updateFilteredUsers()
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
                    chequeAccounts = response.body() ?: emptyList()
                    updateFilteredAccounts()
                    Log.d("ChequeViewModel", "Fetched accounts: $chequeAccounts")
                } else {
                    errorMessage = "API error: ${response.code()}"
                    Log.e("ChequeViewModel", "Failed to fetch accounts: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("ChequeViewModel", "Exception fetching accounts: ${e.message}")
            }
        }
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTransactions()
                if (response.isSuccessful) {
                    transactions = response.body() ?: emptyList()
                    updateFilteredTransactions()
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
                    updateFilteredTransfers()
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
                    updateFilteredPaymentLinks()
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
                    updateFilteredKycRecords()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun fetchRedeemCodeStats() {
        viewModelScope.launch {
            try {
                // Fetch active codes
                val activeResponse = apiService.getActiveCodeCount()
                if (activeResponse.isSuccessful) {
                    activeCodeCount = (activeResponse.body() as? Map<*, *>)?.get("activeCodes") as? Int
                }

                // Fetch total codes
                val totalResponse = apiService.getTotalCodeCount()
                if (totalResponse.isSuccessful) {
                    totalCodeCount = (totalResponse.body() as? Map<*, *>)?.get("totalCodes") as? Int
                }

                // Fetch inactive codes
                val inactiveResponse = apiService.getInactiveCodeCount()
                if (inactiveResponse.isSuccessful) {
                    inactiveCodeCount = (inactiveResponse.body() as? Map<*, *>)?.get("inactiveCodes") as? Int
                }

                // Fetch all codes with users
                val allCodesResponse = apiService.getAllCodesWithUsers()
                if (allCodesResponse.isSuccessful) {
                    allCodes = allCodesResponse.body() ?: emptyList()
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
                    fetchRedeemCodeStats()
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
                val response = apiService.getMyKyc()
                if (response.isSuccessful) {
                    kycName = response.body()?.name ?: "User"
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
    fun createPaymentLink(amount: Double, description: String) {
        viewModelScope.launch {
            try {
                val response = apiService.createPaymentLink(PaymentLinkRequest(amount, description))
                if (response.isSuccessful) {
                    lastCreatedLink = response.body()
                    fetchPaymentLinks()
                    errorMessage = "Payment link created successfully"
                } else {
                    errorMessage = "Failed to create payment link"
                }
            } catch (e: Exception) {
                errorMessage = "Exception: ${e.localizedMessage}"
            }
        }
    }
        fun usePaymentLink(uuid: String) {
            viewModelScope.launch {
                try {
                    val response = apiService.usePaymentLink(uuid)
                    lastUsedLink = response
                    errorMessage = "Payment successful"
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Failed to use payment link"
                }
            }
        }
    fun createTransfer(request: TransferRequest, onSuccess: (TransferResponse) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.createTransfer(request)
                onSuccess(response)
            } catch (e: Exception) {
                onError("Transfer failed: ${e.message}")
            }
        }
    }

    fun loadMyTransfers(onResult: (List<TransferResponse>) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val transfers = apiService.getMyTransfers()
                onResult(transfers)
            } catch (e: Exception) {
                onError("Failed to fetch transfers: ${e.message}")
            }
        }
    }
    }

