package com.example.cheque_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.R
import com.example.cheque_android.data.response.PaymentLinkResponse
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.viewmodel.ChequeViewModel
import kotlinx.coroutines.launch
import com.example.cheque_android.ui.composables.SearchBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPaymentLinksScreen(viewModel: ChequeViewModel, navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val paymentLinks by remember { derivedStateOf { viewModel.filteredPaymentLinks } }
    val error by remember { derivedStateOf { viewModel.errorMessage } }

    LaunchedEffect(Unit) {
        viewModel.clearErrorMessage()
        viewModel.fetchPaymentLinks()
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
                    selected = true,
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
                    title = { Text("Payment Links Management") },
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
                Text("Payment Links Management", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                SearchBar(
                    query = viewModel.paymentLinkSearchQuery,
                    onQueryChange = { viewModel.updatePaymentLinkSearchQuery(it) },
                    placeholder = "Search by ID, account, amount, or UUID"
                )
                Spacer(modifier = Modifier.height(16.dp))
                error?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                if (paymentLinks.isEmpty()) {
                    Text("No payment links found", style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn {
                        items(paymentLinks) { link ->
                            PaymentLinkCard(link, onDelete = { viewModel.deletePaymentLink(it) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentLinkCard(paymentLink: PaymentLinkResponse, onDelete: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = "Payment Link Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
                Column {
                    Text("ID: ${paymentLink.id}", style = MaterialTheme.typography.bodyLarge)
                    Text("Account: ${paymentLink.accountNumber}", style = MaterialTheme.typography.bodyMedium)
                    Text("Amount: $${paymentLink.amount}", style = MaterialTheme.typography.bodyMedium)
                    Text("Description: ${paymentLink.description ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Text("Status: ${paymentLink.status}", style = MaterialTheme.typography.bodyMedium)
                    Text("UUID: ${paymentLink.uuid}", style = MaterialTheme.typography.bodyMedium)
                }
            }
            IconButton(onClick = { onDelete(paymentLink.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}