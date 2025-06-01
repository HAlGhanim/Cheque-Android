package com.example.cheque_android.data.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransferResponse(
    val id: Long,
    val fromUserId: Long,
    val toUserId: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: BigDecimal,
    val transactionId: Long,
    val description: String,
    val createdAt: String
)