package com.example.cheque_android.data.dto

data class DashboardStats(
    val totalUsers: Long,
    val totalTransactions: Int,
    val growthPercentage: Double,
    val lastUpdated: String
)