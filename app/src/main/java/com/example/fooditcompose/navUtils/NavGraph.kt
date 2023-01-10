package com.example.fooditcompose.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.example.authentication.navigationArgs.authScreenComposable
import com.example.common.navigation.splashScreenRoute
import com.example.restaurant.navigation.logInNavComposable
import com.example.user.navigation.profileNavComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost

private const val TransitionDurationMillis = 500

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = splashScreenRoute,
    ) {
        authScreenComposable(navController = navController)
        logInNavComposable(navController = navController)
        profileNavComposable(navController = navController)
    }
}