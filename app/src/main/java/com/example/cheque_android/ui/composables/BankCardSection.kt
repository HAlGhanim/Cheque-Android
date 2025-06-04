package com.example.cheque_android.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cheque_android.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BankCardSection(
    userName: String,
    balance: String,
    cardNumber: String,
    showBalance: Boolean,
    onToggleBalance: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF187C6D), Color(0xFF2DD4C0))
                    )
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnimatedContent(
                            targetState = balance,
                            transitionSpec = { fadeIn() with fadeOut() },
                            label = "Balance"
                        ) { animatedBalance ->
                            Text(animatedBalance, style = MaterialTheme.typography.headlineMedium, color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(onClick = onToggleBalance) {
                            Icon(
                                imageVector = if (showBalance) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle Balance",
                                tint = Color.White
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.chequenocoloredlogo),
                        contentDescription = "Chip Icon",
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(cardNumber, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
        }
    }
}