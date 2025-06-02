package com.example.cheque_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.data.dto.Role
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.ui.composables.LoadingIndicator
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun LoginScreen(viewModel: ChequeViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val user = viewModel.user

    LaunchedEffect(viewModel.token?.token, user) {
        if (!viewModel.token?.token.isNullOrBlank() && user != null) {
            val route = if (user.role == Role.ADMIN) {
                Screen.AdminDashboard.route
            } else {
                Screen.Home.route
            }
            navController.navigate(route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    if (isLoading && user == null) {
        LoadingIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            Text("Don't have an account? Sign up", modifier = Modifier.clickable {
                navController.navigate(Screen.Register.route)
            })
        }
    }
}

