package com.example.fooditcompose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fooditcompose.ui.screens.auth.LoginScreen
import com.example.fooditcompose.ui.screens.auth.RegisterScreen
import com.example.fooditcompose.ui.screens.home.HomeScreen
import com.example.fooditcompose.ui.utils.Screen

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
    ){
        composable(Screen.LoginScreen.route){
            LoginScreen()
        }
        composable(Screen.RegisterScreen.route){
            RegisterScreen()
        }
        composable(Screen.HomeScreen.route){
            HomeScreen()
        }
    }
}