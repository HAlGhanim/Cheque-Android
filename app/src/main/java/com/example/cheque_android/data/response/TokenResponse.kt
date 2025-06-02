package com.example.cheque_android.data.response


data class TokenResponse(
    val token: String?,
    val user: Any? = null

){
    fun getBearerToken(): String {
        return "Bearer $token"
    }
}