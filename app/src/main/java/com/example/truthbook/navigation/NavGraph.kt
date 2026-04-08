package com.example.truthbook.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.truthbook.data.TokenDataStore
import com.example.truthbook.features.auth.LoginScreen
import com.example.truthbook.features.auth.LoginViewModel
import com.example.truthbook.features.auth.SignUpScreen
import com.example.truthbook.features.home.HomeScreen
import kotlinx.coroutines.flow.first

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Check saved token before showing anything
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val token = TokenDataStore.getToken(context).first()
        startDestination = if (!token.isNullOrBlank()) Routes.HOME else Routes.LOGIN
    }

    // Show a blank loading screen until we know where to start
    if (startDestination == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EEF8)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFC4A0DC))
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {

        // 🔹 Login Screen
        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(
                loginViewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(Routes.SIGNUP)
                }
            )
        }

        // 🔹 Signup Screen
        composable(Routes.SIGNUP) {
            SignUpScreen(
                onLoginClick = {
                    navController.popBackStack()
                },
                onSignUpComplete = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 🔹 Home Screen
        // 🔹 Home Screen
        composable(Routes.HOME) {
            HomeScreen(
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }
    }
}