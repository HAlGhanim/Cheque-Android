package com.example.cheque_android.data.response

data class AuthResponse(val token: String?) {
    fun getBearerToken(): String {
        return "Bearer $token"
    }
}