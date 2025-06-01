package com.example.cheque_android.data.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionResponse(
    val id: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: BigDecimal,
    val createdAt: String
)