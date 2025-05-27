package com.example.cheque_android.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheque_android.data.Role
import com.example.cheque_android.data.User
import com.example.cheque_android.data.response.TokenResponse
import com.example.cheque_android.network.ChequeApiService
import com.example.cheque_android.network.RetrofitHelper
import com.example.cheque_android.utils.TokenManager
import kotlinx.coroutines.launch

class ChequeViewModel(private val context: Context) : ViewModel() {
    private val apiService = RetrofitHelper.getInstance(context).create(ChequeApiService::class.java)
    var token: TokenResponse? by mutableStateOf(null)

    fun signup(username: String, password: String, role: Role) {
        viewModelScope.launch {
            try {
                val response = apiService.signup(User(username, password, role, null))
                if (response.isSuccessful) {
                    token = response.body()
                    token?.token?.let {
                        TokenManager.saveToken(context, it)
                        Log.d("Signup", "Token saved")
                    }
                } else {
                    Log.e("Signup", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Signup", "Error: ${e.message}")
            }
        }
    }

    fun login(username: String, password: String, role: Role) {
        viewModelScope.launch {
            try {
                val response = apiService.login(User(username, password, role, null))
                if (response.isSuccessful) {
                    token = response.body()
                    token?.token?.let {
                        TokenManager.saveToken(context, it)
                        Log.d("Login", "Token saved")
                    }
                } else {
                    Log.e("Login", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
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
}
