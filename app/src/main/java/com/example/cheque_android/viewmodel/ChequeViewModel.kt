package com.example.cheque_android.viewmodel

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
import kotlinx.coroutines.launch

class ChequeViewModel : ViewModel() {
    private val apiService = RetrofitHelper.getInstance().create(ChequeApiService::class.java)
    var token: TokenResponse? by mutableStateOf(null)


    fun signup(username: String, password: String, role: Role) {
        viewModelScope.launch {
            try {
                val response = apiService.signup(User(username, password, role, null))
                token = response.body()
            } catch (e: Exception) {
                println("Error $e")
            }

        }
    }

//    fun deposit(amount: Double) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.deposit(token = token?.getBearerToken(), AmountChange(amount))
//
//            } catch (e: Exception) {
//                println("Error $e")
//            }
//
//        }
//    }
// ما رح نستخدم هذا بس موجود عشان نعرف شكل الendpoint الي يحتاج token
}