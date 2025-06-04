package com.example.cheque_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

    val backgroundColor = Color(0xFF292F38)
    val textColor = Color.White
    val buttonColor  = Color(0xFF2ED2C0)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Transfer Funds") },
                navigationIcon = {
                    IconButton(colors = IconButtonDefaults.iconButtonColors(contentColor = buttonColor )
                            ,onClick = { navController.popBackStack() }) {
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
            Image(
                painter = painterResource(com.example.cheque_android.R.drawable.chequecoloredlogo),
                contentDescription = "Cheque Logo",
                modifier = Modifier
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = receiverAccount,
                onValueChange = { receiverAccount = it },
                label = { Text("Receiver Account Number") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFFF5F4FA),
                    unfocusedContainerColor = Color(0xFFF5F4FA),
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFFF5F4FA),
                    unfocusedContainerColor = Color(0xFFF5F4FA),
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                shape = RoundedCornerShape(16.dp),
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFFF5F4FA),
                    unfocusedContainerColor = Color(0xFFF5F4FA),
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
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
                Text("Send Transfer", color = textColor)
            }

            message?.let {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}