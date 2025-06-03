package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.viewmodel.ChequeViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import com.example.cheque_android.ui.composables.SearchBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRedeemScreen(viewModel: ChequeViewModel, navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var amount by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearErrorMessage()
        viewModel.fetchRedeemCodeStats()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Admin Menu", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineSmall)
                Divider()
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = false,
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
                    selected = true,
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
                    title = { Text("Redeem Codes Management") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
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
                SearchBar(
                    query = viewModel.redeemCodeSearchQuery,
                    onQueryChange = { viewModel.updateRedeemCodeSearchQuery(it) },
                    placeholder = "Search by code, amount, or user email"
                )
                Spacer(modifier = Modifier.height(16.dp))

                viewModel.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = if (message.contains("successfully")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Codes: ${viewModel.totalCodeCount ?: 0}")
                        Text("Active Codes: ${viewModel.activeCodeCount ?: 0}")
                        Text("Inactive Codes: ${viewModel.inactiveCodeCount ?: 0}")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val amountBigDecimal = amount.toDoubleOrNull()?.let { BigDecimal(it) } ?: BigDecimal.ZERO
                        if (amountBigDecimal > BigDecimal.ZERO) {
                            viewModel.generateRedeemCode(amountBigDecimal)
                            amount = ""
                        } else {
                            viewModel.errorMessage = "Please enter a valid amount"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = amount.isNotBlank()
                ) {
                    Text("Generate Redeem Code")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("All Redeem Codes", style = MaterialTheme.typography.headlineSmall)

                if (viewModel.filteredRedeemCodes.isEmpty()) {
                    Text("No redeem codes found", style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(viewModel.filteredRedeemCodes) { code ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Code: ${code.code}")
                                    Text("Amount: ${code.amount}")
                                    Text("Status: ${if (code.used) "Used" else "Active"}")
                                    Text("Used By: ${code.userEmail ?: "Not Used"}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}