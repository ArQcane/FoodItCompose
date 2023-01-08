package com.example.user.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.common.navigation.TransitionDurationMillis
import com.example.common.navigation.editProfileScreenRoute
import com.example.common.navigation.loginScreenRoute
import com.example.common.navigation.profileScreenRoute
import com.example.user.profile.ProfileScreen
import com.example.user.profile.editProfile.EditProfileScreen
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
fun NavGraphBuilder.profileNavComposable(navController: NavHostController) {
    composable(
        profileScreenRoute,
        enterTransition = {
            when (initialState.destination.route) {
                profileScreenRoute -> EnterTransition.None
                editProfileScreenRoute -> {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
                }
                else -> {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
                }
            }
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
        },
    ) {
        ProfileScreen(navController = navController)
    }
    composable(
        editProfileScreenRoute,
        enterTransition = { slideIntoContainer(
            towards = AnimatedContentScope.SlideDirection.Left,
            animationSpec = tween(durationMillis = TransitionDurationMillis)
        ) },
    ) {
        EditProfileScreen(navController = navController)
    }
}