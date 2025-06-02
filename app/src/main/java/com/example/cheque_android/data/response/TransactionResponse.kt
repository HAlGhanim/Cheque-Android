package com.example.cheque_android.data.response

import java.math.BigDecimal

data class TransactionResponse(
    val id: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: BigDecimal,
    val createdAt: String
)