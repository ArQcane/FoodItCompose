package com.example.common.navigation

import androidx.navigation.NavHostController

const val splashScreenRoute = "/splash"
const val loginScreenRoute = "/login"
const val resetPasswordFromEmailRoute = "/resetPassword"
const val registerScreenRoute = "/register"
const val homeScreenRoute = "/home"
const val TransitionDurationMillis = 500

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