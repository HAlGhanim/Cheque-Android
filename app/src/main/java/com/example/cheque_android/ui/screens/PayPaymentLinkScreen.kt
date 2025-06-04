package com.example.cheque_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cheque_android.viewmodel.ChequeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPaymentLinkScreen(
    onBack: () -> Unit,
    viewModel: ChequeViewModel
) {
    val backgroundColor = Color(0xFF292F38)
    val textColor = Color.White
    val buttonColor  = Color(0xFF2ED2C0)


    val uuid = remember { mutableStateOf("") }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Use Payment Link") },

                navigationIcon = {
                    IconButton(onClick = onBack,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = buttonColor )
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                },
            )
        },
    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(com.example.cheque_android.R.drawable.chequecoloredlogo),
                contentDescription = "Cheque Logo",
                modifier = Modifier
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = uuid.value,
                onValueChange = { uuid.value = it },
                shape = RoundedCornerShape(16.dp),
                label = { Text("Payment Link UUID") },
                placeholder = { Text("Paste the link UUID here") },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                onClick = {
                    if (uuid.value.isNotBlank()) {
                        viewModel.usePaymentLink(uuid.value)
                    } else {
                        viewModel.errorMessage = "UUID cannot be empty"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            {
                Text("Pay with Link", color = textColor)
            }



        viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = if (it.contains("success", ignoreCase = true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            viewModel.lastUsedLink?.let { link ->
                Spacer(modifier = Modifier.height(12.dp))
                Text("Link Used:", style = MaterialTheme.typography.labelMedium, color = textColor)
                SelectionContainer {
                    Text("UUID: ${link.uuid}", color = buttonColor)
                }
                Text("Amount: ${link.amount} KWD", color = textColor)
                Text("Description: ${link.description}", color = textColor)
            }
        }
    }
}