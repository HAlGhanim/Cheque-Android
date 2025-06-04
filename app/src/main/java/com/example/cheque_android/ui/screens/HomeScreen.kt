package com.example.cheque_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.ui.composables.ActionButton
import com.example.cheque_android.ui.composables.BankCardSection
import com.example.cheque_android.ui.composables.LoadingIndicator
import com.example.cheque_android.ui.composables.SheetTransactionsContent
import com.example.cheque_android.ui.composables.TransactionsCard
import com.example.cheque_android.ui.composables.TypingText
import com.example.cheque_android.viewmodel.ChequeViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ChequeViewModel, navController: NavController) {

    val backgroundColor = Color(0xFF292F38)
    val textColor = Color.White
    val buttonColor  = Color(0xFF2ED2C0)


    val account = viewModel.chequeAccount
    val transactions by remember { derivedStateOf { viewModel.transactions } }

    var showBalance by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val rawBalance = account?.balance?.let { DecimalFormat("#,##0.00").format(it) } ?: "Loading..."
    val displayBalance = if (showBalance) "KD $rawBalance" else "KD *****"
    val cardNumber = if (showBalance) account?.accountNumber ?: "**** **** ****" else "**** **** ****"

    val sheetState = rememberBottomSheetScaffoldState()

    LaunchedEffect(Unit) {
        if (!viewModel.token?.token.isNullOrBlank() && viewModel.user != null) {
            viewModel.getMyAccount()
            viewModel.loadKycData()
        }
    }

    if (!viewModel.isAccountLoaded || viewModel.user == null) {
        LoadingIndicator()
        return
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
                .background(backgroundColor)
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TypingText("Welcome back ${viewModel.kycName}!", speed = 60L)

                Spacer(modifier = Modifier.height(8.dp))

                BankCardSection(
                    userName = viewModel.kycName,
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
                    account?.let {
                        if (it.accountType.equals("MERCHANT", ignoreCase = true)) {
                            ActionButton("Generate Link", icon = {
                                Image(
                                    painter = painterResource(com.example.cheque_android.R.drawable.requestlink),
                                    contentDescription = "Generatelink",
                                    modifier = Modifier
                                        .size(150.dp)
                                )
                            }) {
                                navController.navigate(Screen.GenerateLink.route)
                            }
                        }
                    }

                    ActionButton("Redeem", icon = {
                        Image(
                            painter = painterResource(com.example.cheque_android.R.drawable.redeem),
                            contentDescription = "Redeem",
                            modifier = Modifier
                                .size(150.dp)
                        )
                    }) {
                        navController.navigate(Screen.Redeem.route)
                    }
                    ActionButton("Pay Link", icon = {
                        Image(
                            painter = painterResource(com.example.cheque_android.R.drawable.paylink),
                            contentDescription = "Paylink",
                            modifier = Modifier
                                .size(150.dp)
                        )
                    }) {
                        navController.navigate(Screen.PayPaymentLinkScreen.route)
                    }
                    ActionButton("Transfer", icon = {
                        Image(
                            painter = painterResource(com.example.cheque_android.R.drawable.transaction),
                            contentDescription = "Transfer",
                            modifier = Modifier
                                .size(150.dp)
                        )
                    }) {
                        navController.navigate(Screen.Transfer.route)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Divider(
                    color = Color.White,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(24.dp))

                TransactionsCard(viewModel, account?.accountNumber ?: "")
            }

            FilledTonalButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = buttonColor)
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
