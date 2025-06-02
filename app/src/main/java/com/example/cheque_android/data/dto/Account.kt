package com.example.cheque_android.data.dto

import java.math.BigDecimal

data class AccountResponse(
    val accountNumber: String,
    val userId: Long,
    val balance: BigDecimal,
    val spendingLimit: Int?,
    val accountType: String,
    val createdAt: String
)
