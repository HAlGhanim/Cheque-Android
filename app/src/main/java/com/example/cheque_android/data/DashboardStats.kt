package com.example.cheque_android.data

import java.time.LocalDateTime

data class DashboardStats(
    val totalUsers: Long,
    val totalTransactions: Int,
    val growthPercentage: Double,
    val lastUpdated: LocalDateTime
)