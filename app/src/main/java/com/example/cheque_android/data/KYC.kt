package com.example.cheque_android.data

data class KYC(
    val id: Long = 0,
    val user: User? = null,
    val name: String = "",
    val phone: String? = null
)