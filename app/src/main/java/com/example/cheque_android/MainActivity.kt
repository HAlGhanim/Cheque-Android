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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.cheque_android.navigation.AppNavigation
import com.example.cheque_android.ui.theme.ChequeAndroidTheme
import com.example.cheque_android.viewmodel.ChequeViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChequeAndroidTheme {
                val navController = rememberNavController()
                val viewModel = ChequeViewModel(this)
                viewModel.loadStoredToken()

                if (!viewModel.token?.token.isNullOrBlank()) {
                    MainScope().launch {
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