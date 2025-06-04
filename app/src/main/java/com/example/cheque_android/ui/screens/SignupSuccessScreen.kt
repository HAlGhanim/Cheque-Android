package com.example.cheque_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cheque_android.navigation.Screen

@Composable
fun SignupSuccessScreen(navController: NavController) {
    val backgroundColor = Color(0xFF292F38)
    val textColor = Color.White
    val buttonColor  = Color(0xFF2ED2C0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = buttonColor,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text("You have created a new account", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = textColor
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = buttonColor), onClick = {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }) {
            Text("Go to Login", color = textColor, textAlign = TextAlign.Center)
        }
    }
}