package com.example.cheque_android.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.cheque_android.viewmodel.ChequeViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratePaymentLinkScreen(
    onBack: () -> Unit,
    viewModel: ChequeViewModel
) {
    val accountNumber = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val notificationsEnabled = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current


    val backgroundColor = Color(0xFF292F38)
    val textColor = Color.White
    val buttonColor = Color(0xFF2ED2C0)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("New Payment Link") },
                navigationIcon = {
                    IconButton(colors = IconButtonDefaults.iconButtonColors(contentColor = buttonColor),
                        onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        bottomBar = {
            Button(
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        onClick = {
                    val amountValue = amount.value.toDoubleOrNull()
                    if (amountValue == null || amountValue <= 0.0) {
                        viewModel.errorMessage = "Invalid amount"
                    } else {
                        val desc = "Manual Payment Link to ${accountNumber.value}"
                        viewModel.createPaymentLink(amountValue, desc)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Generate Payment Link", color = textColor)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
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
                value = accountNumber.value,
                onValueChange = { accountNumber.value = it },
                label = { Text("Account Number") },
                placeholder = { Text("Enter account number") },
                trailingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount.value,
                onValueChange = { amount.value = it },
                label = { Text("Amount (KWD)") },
                placeholder = { Text("0.000") },
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    Text("KWD", style = MaterialTheme.typography.bodySmall)
                },
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Payment Notifications", color = textColor)
                Switch(
                    checked = notificationsEnabled.value,
                    onCheckedChange = { notificationsEnabled.value = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = buttonColor,
                        checkedTrackColor = buttonColor.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }

            Text(
                "Receive notifications when this link is paid",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Error or success message
            viewModel.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = if (message.contains("success", ignoreCase = true))
                        buttonColor
                    else
                        MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Display UUID, Copy & Share if link was created
            viewModel.lastCreatedLink?.let { link ->
                Spacer(modifier = Modifier.height(16.dp))
                Text("Generated Link UUID:", style = MaterialTheme.typography.labelMedium, color = textColor)
                SelectionContainer {
                    Text(link.uuid, style = MaterialTheme.typography.bodyLarge,color = textColor)
                }

                Spacer(modifier = Modifier.height(25.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        clipboardManager.setText(AnnotatedString(link.uuid))
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Text("Copy", color = textColor)
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ,onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Payment Link UUID: ${link.uuid}")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Text("Share", color = textColor)
                    }
                }
            }
        }
    }
}