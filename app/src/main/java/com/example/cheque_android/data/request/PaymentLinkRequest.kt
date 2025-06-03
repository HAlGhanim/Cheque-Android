package com.example.cheque_android.data.request

import java.math.BigDecimal

data class PaymentLinkRequest(
    val amount: Double,
    val description: String
)