package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.data.request.TransferRequest
import com.example.cheque_android.viewmodel.ChequeViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    navController: NavController,
    viewModel: ChequeViewModel
) {
    var receiverAccount by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transfer Funds") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = receiverAccount,
                onValueChange = { receiverAccount = it },
                label = { Text("Receiver Account Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    try {
                        val transferRequest = TransferRequest(
                            receiverAccount = receiverAccount,
                            amount = BigDecimal(amount),
                            description = description
                        )
                        viewModel.createTransfer(
                            request = transferRequest,
                            onSuccess = {
                                message = "Transfer successful!"
                                receiverAccount = ""
                                amount = ""
                                description = ""
                            },
                            onError = { error -> message = error }
                        )
                    } catch (e: Exception) {
                        message = "Invalid input. Please check amount format."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Transfer")
            }

            message?.let {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}