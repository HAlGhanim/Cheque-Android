package com.example.cheque_android.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.cheque_android.R
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.viewmodel.ChequeViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ChequeViewModel, navController: NavController) {
    val account = viewModel.chequeAccount
    val transactions by remember { derivedStateOf { viewModel.transactions } }

    var showBalance by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val rawBalance = account?.balance?.let { DecimalFormat("#,##0.00").format(it) } ?: "Loading..."
    val displayBalance = if (showBalance) "KD $rawBalance" else "KD *****"
    val cardNumber = if (showBalance) account?.accountNumber ?: "**** **** ****" else "**** **** ****"

    val sheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getMyAccount()
        viewModel.getMyTransactions()
        viewModel.loadKycData()
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = { SheetTransactionsContent(transactions, account?.accountNumber) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(16.dp)
        ) {
            // Main content area scrolls
            Column(modifier = Modifier.weight(1f)) {
                Text("Hello ${viewModel.kycName ?: "User"}", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                BankCardSection(
                    userName = viewModel.kycName ?: "User",
                    balance = displayBalance,
                    cardNumber = cardNumber,
                    showBalance = showBalance,
                    onToggleBalance = { showBalance = !showBalance }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButton("Transfer", "↓") { navController.navigate(Screen.Transfer.route) }
                    ActionButton("Redeem", "↑") { navController.navigate(Screen.Redeem.route) }
                    ActionButton("Details", "📄") { navController.navigate(Screen.Details.route) }
                    ActionButton("More", "⚙️") { navController.navigate(Screen.More.route) }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { scope.launch { sheetState.bottomSheetState.expand() } },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Transactions", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                        Text("Swipe or tap to view", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Icon(Icons.Filled.ExpandLess, contentDescription = "Expand", tint = Color.Black)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Logout fixed at the bottom
            FilledTonalButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFFEF4444))
            ) {
                Text("Logout", color = Color.White)
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Confirm Logout") },
                    text = { Text("Are you sure you want to logout?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }) { Text("Yes") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BankCardSection(
    userName: String,
    balance: String,
    cardNumber: String,
    showBalance: Boolean,
    onToggleBalance: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                    )
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text("🏦 BANK", style = MaterialTheme.typography.labelLarge, color = Color.White)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnimatedContent(
                            targetState = balance,
                            transitionSpec = { fadeIn() with fadeOut() },
                            label = "Balance"
                        ) { animatedBalance ->
                            Text(animatedBalance, style = MaterialTheme.typography.headlineMedium, color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(onClick = onToggleBalance) {
                            Icon(
                                imageVector = if (showBalance) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle Balance",
                                tint = Color.White
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.chip),
                        contentDescription = "Chip Icon",
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(cardNumber, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
        }
    }
}

@Composable
fun ActionButton(label: String, icon: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE0E7FF),
            modifier = Modifier
                .size(60.dp)
                .clickable { onClick() },
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(icon, style = MaterialTheme.typography.titleLarge, color = Color(0xFF1E3A8A))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Black)
    }
}

@Composable
fun SheetTransactionsContent(
    transactions: List<com.example.cheque_android.data.response.TransactionResponse>,
    accountNumber: String?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Your Transactions", style = MaterialTheme.typography.titleLarge, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(transactions) { tx ->
                val isSender = tx.senderAccountNumber == accountNumber
                val amountPrefix = if (isSender) "- KD" else "+ KD"
                val color = if (isSender) Color(0xFFDC2626) else Color(0xFF16A34A)
                val action = if (isSender) "Send" else "Receive"
                val icon = if (isSender) "⬇️" else "⬆️"

                TransactionCard(
                    item = TransactionItem(
                        type = "Transfer",
                        date = tx.createdAt.substring(0, 16),
                        amount = "$amountPrefix ${tx.amount}",
                        action = action
                    ),
                    icon = icon,
                    amountColor = color
                )
            }
        }
    }
}

@Composable
fun TransactionCard(item: TransactionItem, icon: String, amountColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF9FAFB),
        modifier = Modifier.fillMaxWidth().clickable { },
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.type, style = MaterialTheme.typography.titleSmall, color = Color.Black)
                Text(item.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(item.action, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon)
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.amount, style = MaterialTheme.typography.titleMedium, color = amountColor)
            }
        }
    }
}

data class TransactionItem(val type: String, val date: String, val amount: String, val action: String)