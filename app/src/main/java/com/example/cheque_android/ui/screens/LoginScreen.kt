package com.example.cheque_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    val token = viewModel.token?.token

    LaunchedEffect(token, user, isLoading) {
        if (!token.isNullOrBlank() && user != null && !isLoading) {
            val route = if (user.role == Role.ADMIN) Screen.AdminDashboard.route else Screen.Home.route
            navController.navigate(route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    if (isLoading && user == null) {
        LoadingIndicator()
        return
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF262E38) // Background color like the image
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))
            Text(
                "Login",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Email", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFFF5F4FA),
                    unfocusedContainerColor = Color(0xFFF5F4FA),
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFFF5F4FA),
                    unfocusedContainerColor = Color(0xFFF5F4FA),
                    cursorColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ED2C0)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don't have an account? Sign up",
                color = Color.White,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Register.route)
                }
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Logo (Replace with your image loader if needed)
            Image(
                painter = painterResource(com.example.cheque_android.R.drawable.logo),
                contentDescription = "Cheque Logo",
                modifier = Modifier
                    .size(150.dp)
            )

        }
    }
}