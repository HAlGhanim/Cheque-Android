package com.example.cheque_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cheque_android.ui.screens.HomeScreen
import com.example.cheque_android.ui.screens.LoginScreen
import com.example.cheque_android.ui.screens.RegisterScreen
import com.example.cheque_android.ui.screens.SignupFailureScreen
import com.example.cheque_android.ui.screens.SignupSuccessScreen
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: ChequeViewModel) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.SignupSuccess.route) {
            SignupSuccessScreen(navController = navController)
        }
        composable(Screen.SignupFailureScreen.route) {
            SignupFailureScreen(navController = navController)
        }


    }
}
