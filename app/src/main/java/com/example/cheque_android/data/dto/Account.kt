package com.example.cheque_android.data.dto

data class Account(
    val accountNumber: String,
    val userId: Long,
    val balance: Double,
    val spendingLimit: Int?,
    val accountType: String,
    val createdAt: String
)
