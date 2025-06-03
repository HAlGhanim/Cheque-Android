package com.example.cheque_android.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
    val expiryOption = remember { mutableStateOf("none") }
    val customDate = remember { mutableStateOf("") }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Payment Link") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            Button(
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
                Text("Generate Payment Link")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = accountNumber.value,
                onValueChange = { accountNumber.value = it },
                label = { Text("Account Number") },
                placeholder = { Text("Enter account number") },
                trailingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount.value,
                onValueChange = { amount.value = it },
                label = { Text("Amount (KWD)") },
                placeholder = { Text("0.000") },
                trailingIcon = {
                    Text("KWD", style = MaterialTheme.typography.bodySmall)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Link Expiry", style = MaterialTheme.typography.bodySmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = expiryOption.value == "none",
                    onClick = { expiryOption.value = "none" }
                )
                Text("No Expiry", modifier = Modifier.clickable { expiryOption.value = "none" })
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = expiryOption.value == "custom",
                    onClick = { expiryOption.value = "custom" }
                )
                Text("Custom", modifier = Modifier.clickable { expiryOption.value = "custom" })
            }

            if (expiryOption.value == "custom") {
                OutlinedTextField(
                    value = customDate.value,
                    onValueChange = { customDate.value = it },
                    label = { Text("Custom Expiry Date") },
                    placeholder = { Text("yyyy-mm-dd") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Payment Notifications")
                Switch(
                    checked = notificationsEnabled.value,
                    onCheckedChange = { notificationsEnabled.value = it }
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
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Display UUID, Copy & Share if link was created
            viewModel.lastCreatedLink?.let { link ->
                Spacer(modifier = Modifier.height(16.dp))
                Text("Generated Link UUID:", style = MaterialTheme.typography.labelMedium)
                SelectionContainer {
                    Text(link.uuid, style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        clipboardManager.setText(AnnotatedString(link.uuid))
                    }) {
                        Text("Copy")
                    }

                    Button(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Payment Link UUID: ${link.uuid}")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Text("Share")
                    }
                }
            }
        }
    }
}