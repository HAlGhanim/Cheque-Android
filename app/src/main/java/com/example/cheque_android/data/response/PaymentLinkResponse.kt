package com.example.cheque_android.data.response

import java.math.BigDecimal

data class PaymentLinkResponse(
    val id: Long,
    val accountNumber: String,
    val amount: BigDecimal,
    val description: String,
    val status: String,
    val transactionId: Long?,
    val uuid: String
)