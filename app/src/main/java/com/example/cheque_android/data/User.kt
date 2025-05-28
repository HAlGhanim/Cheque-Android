package com.example.cheque_android.data

data class User(
    var email: String,
    var password: String,
    val role: Role? = null,
    val token: String? = null
)

enum class Role {
    USER,ADMIN
}