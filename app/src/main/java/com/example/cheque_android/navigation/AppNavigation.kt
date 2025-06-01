package com.example.cheque_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cheque_android.ui.screens.AdminAccountsScreen
import com.example.cheque_android.ui.screens.AdminDashboardScreen
import com.example.cheque_android.ui.screens.AdminUsersScreen
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

    }
}
