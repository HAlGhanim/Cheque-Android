package com.example.cheque_android.data.request

import java.math.BigDecimal

data class TransferRequest(
    val receiverAccount: String,
    val amount: BigDecimal,
    val description: String
)