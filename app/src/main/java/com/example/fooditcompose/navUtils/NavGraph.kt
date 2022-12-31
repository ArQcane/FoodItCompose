package com.example.fooditcompose.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.example.authentication.navigationArgs.authScreenComposable
import com.example.authentication.navigationArgs.splashScreenRoute
import com.example.common.utils.Screen
import com.example.fooditcompose.ui.screens.home.HomeScreen
import com.example.fooditcompose.ui.screens.profile.ProfileScreen
import com.example.fooditcompose.ui.screens.search.SearchScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

private const val TransitionDurationMillis = 500

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = splashScreenRoute,
    ) {
        authScreenComposable(navController = navController)
        composable(
            Screen.HomeScreen.route,
            enterTransition = {
                when(initialState.destination.route){
                    splashScreenRoute -> expandIn(animationSpec = tween(500))
                    "login" -> expandIn(animationSpec = tween(500))
                    Screen.HomeScreen.route -> EnterTransition.None
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
            Screen.SearchScreen.route,
            enterTransition = {
                when(initialState.destination.route){
                    Screen.SearchScreen.route -> EnterTransition.None
                    Screen.HomeScreen.route -> {
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
                when(targetState.destination.route){
                    Screen.ProfileScreen.route -> {
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
                when(targetState.destination.route){
                    Screen.ProfileScreen.route -> {
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
        ){
            SearchScreen(navController = navController)
        }
        composable(
            Screen.ProfileScreen.route,
            enterTransition = {
                when(initialState.destination.route){
                    Screen.ProfileScreen.route -> EnterTransition.None
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
        ){
            ProfileScreen(navController = navController)
        }
    }
}