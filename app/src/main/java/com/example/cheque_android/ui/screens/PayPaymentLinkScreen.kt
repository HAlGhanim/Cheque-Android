package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cheque_android.viewmodel.ChequeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPaymentLinkScreen(
    onBack: () -> Unit,
    viewModel: ChequeViewModel
) {
    val uuid = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Use Payment Link") },
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
                    if (uuid.value.isNotBlank()) {
                        viewModel.usePaymentLink(uuid.value)
                    } else {
                        viewModel.errorMessage = "UUID cannot be empty"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Pay with Link")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uuid.value,
                onValueChange = { uuid.value = it },
                label = { Text("Payment Link UUID") },
                placeholder = { Text("Paste the link UUID here") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = if (it.contains("success", ignoreCase = true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            viewModel.lastUsedLink?.let { link ->
                Spacer(modifier = Modifier.height(12.dp))
                Text("Link Used:", style = MaterialTheme.typography.labelMedium)
                SelectionContainer {
                    Text("UUID: ${link.uuid}")
                }
                Text("Amount: ${link.amount} KWD")
                Text("Description: ${link.description}")
            }
        }
    }
}