package com.example.cheque_android.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cheque_android.data.dto.Transaction

@Composable
fun TransactionCard(item: Transaction, icon: String, amountColor: Color) {
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
