package com.example.authentication.navigationArgs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.authentication.forgetPassword.ResetPasswordWithEmailScreen
import com.example.authentication.login.LoginScreen
import com.example.authentication.register.RegisterScreen
import com.example.authentication.splashAnims.SplashScreen
import com.example.common.utils.Screen

const val splashScreenRoute = "/splash"
internal const val loginScreenRoute = "/login"
internal const val resetPasswordFromEmailRoute = "/resetPassword"
internal const val registerScreenRoute = "/register"

fun NavHostController.navigateToAuthScreen() {
    navigate(loginScreenRoute)
}

fun NavGraphBuilder.authScreenComposable(navController: NavHostController) {
    composable(splashScreenRoute) {
        SplashScreen(navController = navController)
    }
    composable(loginScreenRoute) {
        LoginScreen(navController = navController)
    }
    composable(registerScreenRoute) {
        RegisterScreen(navController)
    }
    composable(resetPasswordFromEmailRoute){
        ResetPasswordWithEmailScreen(navController = navController)
    }
}
