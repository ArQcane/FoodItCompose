package com.example.restaurant.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.navigation.*
import com.example.common.navigation.*
import com.example.restaurant.home.HomeScreen
import com.example.restaurant.restaurantDetails.SpecificRestaurantScreen
import com.example.restaurant.restaurantDetails.reviews.create.CreateReviewScreen
import com.example.restaurant.search.SearchScreen
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.logInNavComposable(navController: NavHostController) {
    composable(
        homeScreenRoute,
        enterTransition = {
            when (initialState.destination.route) {
                splashScreenRoute -> expandIn(animationSpec = tween(500))
                "login" -> expandIn(animationSpec = tween(500))
                homeScreenRoute -> EnterTransition.None
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
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = TransitionDurationMillis)
            )
        },
    ) {
        HomeScreen(navController = navController)
    }
    composable(
        restaurantDetailRoute,
        arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
    ) { backStackEntry ->
        backStackEntry?.arguments?.getString("restaurantId")?.let { restaurantId ->
            SpecificRestaurantScreen(navController = navController)
        }
    }
    composable(
        createReviewRoute,
        arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
        ) {backStackEntry ->
        backStackEntry?.arguments?.getString("restaurantId")?.let { restaurantId ->
            CreateReviewScreen(navController = navController)
        }
    }
    composable(
        searchScreenRoute,
        enterTransition = {
            when (initialState.destination.route) {
                searchScreenRoute -> EnterTransition.None
                homeScreenRoute -> {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
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
            when (targetState.destination.route) {
                profileScreenRoute -> {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
                }
                else -> {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
                }
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                profileScreenRoute -> {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
                }
                else -> {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = TransitionDurationMillis)
                    )
                }
            }
        },
    ) {
        SearchScreen(navController = navController)
    }
}