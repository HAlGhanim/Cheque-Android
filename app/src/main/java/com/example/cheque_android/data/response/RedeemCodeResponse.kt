package com.example.cheque_android.data.response

import java.math.BigDecimal

data class RedeemCodeResponse(
    val id: Long,
    val code: String,
    val amount: BigDecimal,
    val used: Boolean,
    val userEmail: String?
)