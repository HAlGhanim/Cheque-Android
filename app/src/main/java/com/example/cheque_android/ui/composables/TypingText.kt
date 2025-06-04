package com.example.cheque_android.ui.composables

import android.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypingText(text: String, speed: Long = 50L) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        visibleText = ""
        for (i in text.indices) {
            visibleText += text[i]
            delay(speed)
        }
    }

    Text(
        text = visibleText,
        color = Color.White,
        fontSize = 25.sp,
        style = MaterialTheme.typography.bodyLarge.copy(
            textDirection = TextDirection.Ltr
        ),
        textAlign = TextAlign.Start
    )
}