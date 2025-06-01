package com.example.cheque_android.data

import com.google.gson.annotations.SerializedName

data class User(
    var email: String,
    var password: String,
    val id: Long? = null,
    val role: Role? = null,
    val status: String = "Active",
    val token: String? = null
)

enum class Role {
    @SerializedName
        ("USER")
    USER,

    @SerializedName("ADMIN")
    ADMIN
}