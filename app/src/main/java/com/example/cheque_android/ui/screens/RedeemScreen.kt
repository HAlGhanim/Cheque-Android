package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun RedeemScreen(navController: NavController, viewModel: ChequeViewModel) {
    var code by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Back Button
        IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Redeem Code", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Enter Redeem Code") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.redeemCode(code) {
                        resultMessage = it
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Redeem")
            }

            resultMessage?.let {
                Spacer(modifier = Modifier.height(24.dp))
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
