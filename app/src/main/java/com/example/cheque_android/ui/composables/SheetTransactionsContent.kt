package com.example.cheque_android.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cheque_android.data.dto.Transaction
import com.example.cheque_android.data.response.TransactionResponse

@Composable
fun SheetTransactionsContent(
    transactions: List<TransactionResponse>,
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
                    item = Transaction(
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