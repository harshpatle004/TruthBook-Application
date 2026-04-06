package com.example.truthbook.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.truthbook.features.auth.LoginScreen
import com.example.truthbook.features.auth.LoginViewModel
import com.example.truthbook.features.auth.SignUpScreen
import com.example.truthbook.features.home.HomeScreen

@Composable
fun NavGraph() {


    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
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
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 🔹 Home Screen
        composable(Routes.HOME) {
            HomeScreen()
        }
    }


}
