package com.example.cheque_android.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun TransactionsCard(viewModel: ChequeViewModel, accountNumber: String) {

    val textColor = Color.White
    val buttonColor  = Color(0xFF2ED2C0)

    val transactions = viewModel.transactions.sortedByDescending { it.createdAt }

    LaunchedEffect(accountNumber) {
        if (accountNumber.isNotBlank()) {
            viewModel.getTransactionsByAccount(accountNumber)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
            color = buttonColor,
            fontSize = 25.sp
        )

        if (transactions.isEmpty()) {
            Text(
                text = "No transactions available.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = textColor

            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(transactions) { tx ->
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("From: ${tx.senderAccountNumber}", style = MaterialTheme.typography.bodyMedium,color = textColor)
                        Text("To: ${tx.receiverAccountNumber}", style = MaterialTheme.typography.bodyMedium,color = textColor)
                        Text("Amount: KD ${tx.amount}", style = MaterialTheme.typography.bodyMedium,color = textColor)
                        Text("Date: ${tx.createdAt}", style = MaterialTheme.typography.bodySmall,color = textColor)
                    }
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}