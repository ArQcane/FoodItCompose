package com.example.authentication.navigationArgs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.authentication.forgetPassword.ResetPasswordWithEmailScreen
import com.example.authentication.login.LoginScreen
import com.example.authentication.register.RegisterScreen
import com.example.authentication.splashAnims.SplashScreen
import com.example.common.navigation.*
import com.google.accompanist.navigation.animation.composable

fun NavHostController.navigateToAuthScreen(
    popUpTo: String? = null
) {
    navigate(loginScreenRoute) {
        popUpTo ?: return@navigate
        popUpTo(popUpTo) {
            inclusive = true
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authScreenComposable(navController: NavHostController) {
    composable(
        splashScreenRoute
    ) {
        SplashScreen(navController = navController)
    }
    composable(
        loginScreenRoute,
        enterTransition = {
            when(initialState.destination.route){
                splashScreenRoute -> {
                    fadeIn(animationSpec = tween(250, delayMillis = 450))
                }
                else -> {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
                }
            }
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        },
    ) {
        LoginScreen(navController = navController)
    }
    composable(
        registerScreenRoute,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        }
    ) {
        RegisterScreen(navController)
    }
    composable(
        resetPasswordFromEmailRoute,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        }) {
        ResetPasswordWithEmailScreen(navController = navController)
    }
}

