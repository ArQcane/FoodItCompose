package com.example.fooditcompose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authentication.forgetPassword.ResetPasswordWithEmailScreen
import com.example.authentication.login.LoginScreen
import com.example.authentication.navigationArgs.authScreenComposable
import com.example.authentication.navigationArgs.splashScreenRoute
import com.example.authentication.register.RegisterScreen
import com.example.common.utils.Screen
import com.example.fooditcompose.ui.screens.home.HomeScreen
import com.example.fooditcompose.ui.screens.profile.ProfileScreen
import com.example.fooditcompose.ui.screens.search.SearchScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = splashScreenRoute,
    ) {
        authScreenComposable(navController = navController)
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route){
            ProfileScreen(navController = navController)
        }
        composable(Screen.SearchScreen.route){
            SearchScreen(navController = navController)
        }
    }
}