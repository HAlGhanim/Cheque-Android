package com.example.cheque_android.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Register: Screen("register")
    object SignupSuccess: Screen("signupsuccess")
    object SignupFailureScreen: Screen("signupfailurescreen")
}
