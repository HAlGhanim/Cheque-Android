package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.R
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.ui.composables.StatRow
import com.example.cheque_android.utils.formatDate
import com.example.cheque_android.viewmodel.ChequeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: ChequeViewModel, navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.clearErrorMessage()
        viewModel.fetchDashboardStats()
        viewModel.fetchRedeemCodeStats() // Fetch redeem code stats
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Admin Menu", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineSmall)
                Divider()
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminDashboard.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Users") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminUsers.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Accounts") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminAccounts.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Transactions") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminTransactions.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Transfers") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminTransfers.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Payment Links") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminPaymentLinks.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("KYC") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminKYC.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Redeem Codes") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminRedeem.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Admin Dashboard") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                viewModel.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                viewModel.dashboardStats?.let { stats ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(30.dp)) {
                            StatRow("Total Users", stats.totalUsers.toString(), R.drawable.idcard)
                            StatRow("Total Transactions", stats.totalTransactions.toString(), R.drawable.bankstatement)
                            StatRow("Total Codes", viewModel.totalCodeCount?.toString() ?: "0", R.drawable.card)
                            StatRow("Active Codes", viewModel.activeCodeCount?.toString() ?: "0", R.drawable.card)
                            StatRow("Inactive Codes", viewModel.inactiveCodeCount?.toString() ?: "0", R.drawable.card)
                        }
                    }
                } ?: run {
                    Text("Loading dashboard stats...", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}