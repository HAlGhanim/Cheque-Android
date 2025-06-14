package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.R
import com.example.cheque_android.data.dto.Account
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.utils.formatDate
import com.example.cheque_android.viewmodel.ChequeViewModel
import kotlinx.coroutines.launch
import com.example.cheque_android.ui.composables.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAccountsScreen(viewModel: ChequeViewModel, navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.clearErrorMessage()
        viewModel.fetchAccounts()
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
                    selected = true,
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
                    title = { Text("Accounts Management") },
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
                SearchBar(
                    query = viewModel.accountSearchQuery,
                    onQueryChange = { viewModel.updateAccountSearchQuery(it) },
                    placeholder = "Search by account number, user ID, or type"
                )
                Spacer(modifier = Modifier.height(16.dp))
                viewModel.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                if (viewModel.filteredChequeAccounts.isEmpty()) {
                    Text("No accounts found", style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn {
                        items(viewModel.filteredChequeAccounts) { account ->
                            AccountCard(account)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountCard(chequeAccount: Account) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.wallet),
                contentDescription = "Account Icon",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text("Account: ${chequeAccount.accountNumber}", style = MaterialTheme.typography.bodyLarge)
                Text("User ID: ${chequeAccount.userId}", style = MaterialTheme.typography.bodyMedium)
                Text("Balance: $${chequeAccount.balance}", style = MaterialTheme.typography.bodyMedium)
                Text("Type: ${chequeAccount.accountType}", style = MaterialTheme.typography.bodyMedium)
                Text("Created: ${formatDate(chequeAccount.createdAt)}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}