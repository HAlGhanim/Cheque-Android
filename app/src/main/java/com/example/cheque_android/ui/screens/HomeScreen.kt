package com.example.cheque_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheque_android.data.Role
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun HomeScreen(viewModel: ChequeViewModel, navController: NavController) {
    LaunchedEffect(viewModel.user) {
        if (viewModel.user?.role != Role.ADMIN) {
            viewModel.errorMessage = "Access denied: Admins only"
//            viewModel.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
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
        Text("Welcome, Admin!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                navController.navigate(Screen.AdminDashboard.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Dashboard")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
