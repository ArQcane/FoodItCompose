package com.example.fooditcompose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fooditcompose.ui.screens.auth.LoginScreen
import com.example.fooditcompose.ui.screens.auth.RegisterScreen
import com.example.fooditcompose.ui.screens.auth.ResetPasswordWithEmailScreen
import com.example.fooditcompose.ui.screens.home.HomeScreen
import com.example.fooditcompose.ui.utils.Screen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(
            Screen.ResetPasswordFromLink.route,
            arguments = listOf(
                navArgument("email") {
                    defaultValue = ""
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val email = entry.arguments?.getString("email")
            ResetPasswordWithEmailScreen(email = email, navController = navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen()
        }
    }
}