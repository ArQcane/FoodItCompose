package com.example.authentication.navigationArgs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.authentication.forgetPassword.ResetPasswordWithEmailScreen
import com.example.authentication.login.LoginScreen
import com.example.authentication.register.RegisterScreen
import com.example.authentication.splashAnims.SplashScreen
import com.example.common.utils.Screen
import com.google.accompanist.navigation.animation.composable

const val splashScreenRoute = "/splash"
internal const val loginScreenRoute = "/login"
internal const val resetPasswordFromEmailRoute = "/resetPassword"
internal const val registerScreenRoute = "/register"
internal const val homeScreenRoute = "/home"
private const val TransitionDurationMillis = 500

fun NavHostController.navigateToAuthScreen(
    shouldPopBackStack: Boolean = false,
    popUpTo: String = splashScreenRoute
) {
    navigate(loginScreenRoute) {
        if (!shouldPopBackStack) return@navigate
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

