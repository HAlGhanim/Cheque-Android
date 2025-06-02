package com.example.cheque_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cheque_android.R
import com.example.cheque_android.navigation.Screen
import com.example.cheque_android.viewmodel.ChequeViewModel

@Composable
fun RegisterScreen(viewModel: ChequeViewModel, navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 35.sp, color = Color(0xFF384BAB))

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)         )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)         )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)         )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)         )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Who are you?", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF384BAB))

        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.merchant),
                    contentDescription = "Merchant",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(75.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                RoleOption(
                    "merchant",
                    "Merchant",
                    "Send and receive money easily",
                    selectedRole
                ) { selectedRole = it }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Image(
                    painter = painterResource(R.drawable.customer),
                    contentDescription = "Customer",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(75.dp)
                )
            }

            item {
                RoleOption(
                    "customer",
                    "Customer",
                    "Easy send money to your favourite store",
                    selectedRole
                ) { selectedRole = it }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = acceptTerms, onCheckedChange = { acceptTerms = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("I accept Cheque Terms & Conditions and Privacy Policy")
        }

        Spacer(modifier = Modifier.height(24.dp))

        val allValid = fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && acceptTerms && selectedRole.isNotBlank() && number.isNotBlank()

        if (errorMessage != null) {
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                isLoading = true
                viewModel.registerFullFlow(
                    name = fullName,
                    phone = number,
                    email = email,
                    password = password,
                    role = selectedRole,
                    onSuccess = {
                        isLoading = false
                        navController.navigate(Screen.SignupSuccess.route)                    },
                    onError = {
                        isLoading = true
                        errorMessage = it
                    }
                )
            },
            enabled = allValid && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Signing up..." else "Sign up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Already have an account? Sign in", modifier = Modifier.clickable {
            navController.popBackStack()
        })
    }
}

@Composable
fun RoleOption(roleValue: String, title: String, subtitle: String, selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .selectable(
                selected = selected == roleValue,
                onClick = { onSelect(roleValue) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected == roleValue,
            onClick = null // handled by Row
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}