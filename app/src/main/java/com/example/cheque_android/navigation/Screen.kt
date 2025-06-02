package com.example.cheque_android.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object AdminDashboard : Screen("admin_dashboard")
    object AdminUsers : Screen("admin_users")
    object AdminAccounts : Screen("admin_accounts")
    object AdminTransactions : Screen("admin_transactions")
    object AdminTransfers : Screen("admin_transfers")
    object AdminPaymentLinks : Screen("admin_payment_links")
    object AdminKYC : Screen("admin_kyc")
    object AdminRedeem : Screen("admin_redeem")
    object Transfer : Screen("transfer")
    object Register: Screen("register")
    object SignupSuccess: Screen("signupsuccess")
    object SignupFailureScreen: Screen("signupfailurescreen")
}

