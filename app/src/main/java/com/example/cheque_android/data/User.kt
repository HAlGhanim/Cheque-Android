package com.example.cheque_android.data

data class User(
    var email: String,
    var password: String,
    var role: Role = Role.USER,
    var token: String?
)

enum class Role {
    USER,ADMIN
}