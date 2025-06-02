package com.example.cheque_android.data.response

import com.example.cheque_android.data.dto.User

data class KYCResponse(
    val id: Long,
    val user: User,
    val name: String,
    val phone: String
)