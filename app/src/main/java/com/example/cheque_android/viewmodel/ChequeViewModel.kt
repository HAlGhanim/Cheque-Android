package com.example.cheque_android.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheque_android.data.AccountRequest
import com.example.cheque_android.data.KYCRequest
import com.example.cheque_android.data.RegisterRequests
import com.example.cheque_android.data.User
import com.example.cheque_android.data.response.TokenResponse
import com.example.cheque_android.network.ChequeApiService
import com.example.cheque_android.network.RetrofitHelper
import com.example.cheque_android.utils.TokenManager
import kotlinx.coroutines.launch

class ChequeViewModel(private val context: Context) : ViewModel() {

    private val apiService =
        RetrofitHelper.getInstance(context).create(ChequeApiService::class.java)

    var token: TokenResponse? by mutableStateOf(null)
    var name by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null)


    fun loadStoredToken() {
        token = TokenResponse(TokenManager.getToken(context))
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(User(username, password))
                if (response.isSuccessful) {
                    token = response.body()
                    token?.token?.let {
                        TokenManager.saveToken(context, it)
                        loginError = null // clear previous errors if any
                        Log.d("Login", "Token saved")
                    }
                } else {
                    loginError = "Login failed: ${response.code()} ${response.message()}"
                    Log.e("Login", "Failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                loginError = "An error occurred: ${e.localizedMessage ?: "Unknown error"}"
                Log.e("Login", "Error: ${e.message}")
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
                val registerRequest = RegisterRequests(email = email, password = password)
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
                    token = "Bearer $extractedToken",
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