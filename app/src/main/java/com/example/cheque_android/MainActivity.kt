package com.example.cheque_android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.cheque_android.navigation.AppNavigation
import com.example.cheque_android.ui.theme.ChequeAndroidTheme
import com.example.cheque_android.viewmodel.ChequeViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChequeAndroidTheme {
                val navController = rememberNavController()
                val viewModel = ChequeViewModel(this)

                LaunchedEffect(viewModel.token) {
                    if (!viewModel.token?.token.isNullOrBlank()) {
                        navController.navigate("home") {
                            popUpTo(0)
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}