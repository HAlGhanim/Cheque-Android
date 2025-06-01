package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.data.Role
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun LoginScreen(viewModel: ChequeViewModel, navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("KAlGhanim8@gmail.com") }
    var password by remember { mutableStateOf("123456") }

    LaunchedEffect(viewModel.token) {
        if (!viewModel.token?.token.isNullOrBlank()) {
            val route = if (viewModel.user?.role == Role.ADMIN) {
                Screen.AdminDashboard.route
            } else {
                viewModel.errorMessage = "Access denied: Admins only"
                Screen.Login.route
            }
            navController.navigate(route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Admin Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        viewModel.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.login(username, password) { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}