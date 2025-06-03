package com.example.cheque_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cheque_android.navigation.Screen.PolicyScreen
import com.example.cheque_android.ui.screens.AdminAccountsScreen
import com.example.cheque_android.ui.screens.AdminDashboardScreen
import com.example.cheque_android.ui.screens.AdminKYCScreen
import com.example.cheque_android.ui.screens.AdminPaymentLinksScreen
import com.example.cheque_android.ui.screens.AdminRedeemScreen
import com.example.cheque_android.ui.screens.AdminTransactionsScreen
import com.example.cheque_android.ui.screens.AdminTransfersScreen
import com.example.cheque_android.ui.screens.AdminUsersScreen
import com.example.cheque_android.ui.screens.HomeScreen
import com.example.cheque_android.ui.screens.LoginScreen
import com.example.cheque_android.ui.screens.PolicyScreen
import com.example.cheque_android.ui.screens.RedeemScreen
import com.example.cheque_android.ui.screens.TransferPaymentScreen
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
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.AdminUsers.route) {
            AdminUsersScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.AdminAccounts.route) {
            AdminAccountsScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.AdminTransactions.route) {
            AdminTransactionsScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.AdminTransfers.route) {
            AdminTransfersScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.AdminPaymentLinks.route) {
            AdminPaymentLinksScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.AdminKYC.route) {
            AdminKYCScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.AdminRedeem.route) {
            AdminRedeemScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Transfer.route) {
            TransferPaymentScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.SignupSuccess.route) {
            SignupSuccessScreen(navController = navController)
        }
        composable(Screen.SignupFailureScreen.route) {
            SignupFailureScreen(navController = navController)
        }
        composable(Screen.Redeem.route){
            RedeemScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.PolicyScreen.route){
            PolicyScreen(navController = navController)
        }
    }
}