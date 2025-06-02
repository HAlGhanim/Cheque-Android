package com.example.cheque_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferPaymentScreen(onBack: () -> Unit) {
    val accountNumber = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val notificationsEnabled = remember { mutableStateOf(false) }
    val expiryOption = remember { mutableStateOf("none") }
    val customDate = remember { mutableStateOf("") }

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
                onClick = { /* Handle generate link */ },
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
        }
    }
}
