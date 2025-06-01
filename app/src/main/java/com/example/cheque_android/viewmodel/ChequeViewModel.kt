package com.example.cheque_android.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheque_android.data.AccountResponse
import com.example.cheque_android.data.DashboardStats
import com.example.cheque_android.data.KYC
import com.example.cheque_android.data.Role
import com.example.cheque_android.data.User
import com.example.cheque_android.data.request.RedeemRequest
import com.example.cheque_android.data.response.AuthResponse
import com.example.cheque_android.data.response.PaymentLinkResponse
import com.example.cheque_android.data.response.TransactionResponse
import com.example.cheque_android.data.response.TransferResponse
import com.example.cheque_android.network.ChequeApiService
import com.example.cheque_android.network.RetrofitHelper
import com.example.cheque_android.utils.TokenManager
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ChequeViewModel(private val context: Context) : ViewModel() {
    private val apiService = RetrofitHelper.getInstance(context).create(ChequeApiService::class.java)
    var token: AuthResponse? by mutableStateOf(null)
    var user: User? by mutableStateOf(null)
    var usersList: List<User> by mutableStateOf(emptyList()) // State to hold the list of users
    var errorMessage: String? by mutableStateOf(null) // State to hold error messages

    init {
        loadStoredToken()
    }

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


    fun loadStoredToken() {
        token = AuthResponse(TokenManager.getToken(context))
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
                    logout() // Clear token if user fetch fails (e.g., token expired)
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
                if (response.isSuccessful) {
                    token = response.body()
                    token?.token?.let {
                        TokenManager.saveToken(context, it)
                        Log.d("Login", "Token saved: $it")
                        val userResponse = apiService.getCurrentUser()
                        Log.d("Login", "getCurrentUser response: isSuccessful=${userResponse.isSuccessful}, code=${userResponse.code()}, body=${userResponse.body()}")
                        if (userResponse.isSuccessful) {
                            user = userResponse.body()
                            Log.d("Login", "User fetched: email=${user?.email}, role=${user?.role}")
                            user?.role?.let { role ->
                                val route = if (role == Role.ADMIN) "admin_dashboard" else "home"
                                Log.d("Login", "Navigating to route: $route")
                                onNavigate(route)
                            } ?: run {
                                Log.e("Login", "User role is null, defaulting to home")
                                onNavigate("home")
                            }
                        } else {
                            Log.e("Login", "Failed to fetch user: ${userResponse.code()} - ${userResponse.errorBody()?.string()}")
                            onNavigate("home")
                        }
                    } ?: run {
                        Log.e("Login", "Token is null")
                        onNavigate("home")
                    }
                } else {
                    Log.e("Login", "Login failed: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Login", "Error: ${e.message}", e)
            }
        }
    }

    fun logout() {
        TokenManager.clearToken(context)
        token = null
        Log.d("Logout", "Token cleared")
    }

    fun getMyAccount() {
        viewModelScope.launch {
            try {
                val response = apiService.getMyAccount()
                Log.e("getMyAccount", response.isSuccessful.toString())
            } catch (e: Exception) {
                Log.e("getMyAccount", e.message.toString())
            }
        }
    }

    // Admin Functions
    fun fetchDashboardStats() {
        viewModelScope.launch {
            try {
                val response = apiService.getDashboardStats()
                if (response.isSuccessful) {
                    dashboardStats = response.body()
                } else {
                    Log.e("Dashboard", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Dashboard", "Error: ${e.message}")
            }
        }
    }

    fun fetchUsers(page: Int = 1, size: Int = 10, role: String? = null) {
        viewModelScope.launch {
            try {
                val response = apiService.getUsers(page, size, role)
                if (response.isSuccessful) {
                    users = response.body() ?: emptyList()
                } else {
                    Log.e("Users", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Users", "Error: ${e.message}")
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteUser(userId)
                if (response.isSuccessful) {
                    fetchUsers() // Refresh list
                } else {
                    Log.e("DeleteUser", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("DeleteUser", "Error: ${e.message}")
            }
        }
    }

    fun fetchAccounts() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllAccounts()
                if (response.isSuccessful) {
                    accounts = response.body() ?: emptyList()
                } else {
                    Log.e("Accounts", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Accounts", "Error: ${e.message}")
            }
        }
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTransactions()
                if (response.isSuccessful) {
                    transactions = response.body() ?: emptyList()
                } else {
                    Log.e("Transactions", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Transactions", "Error: ${e.message}")
            }
        }
    }

    fun fetchTransfers() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTransfers()
                if (response.isSuccessful) {
                    transfers = response.body() ?: emptyList()
                } else {
                    Log.e("Transfers", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Transfers", "Error: ${e.message}")
            }
        }
    }

    fun fetchPaymentLinks() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllPaymentLinks()
                if (response.isSuccessful) {
                    paymentLinks = response.body() ?: emptyList()
                } else {
                    Log.e("PaymentLinks", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PaymentLinks", "Error: ${e.message}")
            }
        }
    }

    fun fetchKYC() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllKYC()
                if (response.isSuccessful) {
                    kycRecords = response.body() ?: emptyList()
                } else {
                    Log.e("KYC", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("KYC", "Error: ${e.message}")
            }
        }
    }

    fun fetchActiveCodeCount() {
        viewModelScope.launch {
            try {
                val response = apiService.getActiveCodeCount()
                if (response.isSuccessful) {
                    activeCodeCount = response.body()?.get("activeCodes")
                } else {
                    Log.e("RedeemCount", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RedeemCount", "Error: ${e.message}")
            }
        }
    }

    fun generateRedeemCode(amount: BigDecimal) {
        viewModelScope.launch {
            try {
                val response = apiService.generateRedeemCode(RedeemRequest(amount))
                if (response.isSuccessful) {
                    generatedCode = response.body()
                } else {
                    Log.e("GenerateCode", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("GenerateCode", "Error: ${e.message}")
            }
        }
    }








}