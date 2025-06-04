package com.example.cheque_android.ui.screens

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.navigation.Screen

@Composable
fun PolicyScreen(navController: NavController) {
    val backgroundColor = Color(0xFF292F38)
    val textColor = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        ) {
        Text(
            text = "Terms and Conditions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        val policies = listOf(
            "1. Financial Limit" to "The default financial limit for each user is set at KWD 3,000. This limit is subject to increase or decrease based on the user’s request and subject to approval by our financial team. Any adjustments will require proper verification and may include credit or risk assessment.",
            "2. Account Responsibility" to "Users are responsible for maintaining the confidentiality of their account credentials and for all activities conducted through their account.",
            "3. Use of Services" to "The application is intended solely for personal financial management and services provided within the app. Any misuse or unauthorized activity may result in suspension or termination of the account.",
            "4. Data Privacy" to "All personal and financial information is stored securely and will not be shared with third parties except as required by law or with user consent.",
            "5. Modification of Terms" to "We reserve the right to update or modify these Terms and Conditions at any time. Continued use of the application after such changes implies acceptance of the revised terms.",
            "6. Governing Law" to "These Terms and Conditions shall be governed by and construed in accordance with the laws of the State of Kuwait."
        )

        policies.forEach { (title, description) ->
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 12.dp),
                color = textColor

            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
                color = textColor

            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(Screen.Register.route) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ED2C0))
        ) {
            Text("Back")
        }
    }
}