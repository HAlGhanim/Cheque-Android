package com.example.cheque_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cheque_android.ui.screens.HomeScreen
import com.example.cheque_android.ui.screens.LoginScreen
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: ChequeViewModel) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }

    }
}
