package com.example.fooditcompose.ui.utils

sealed class Screen (val route: String){
    object SplashScreen : Screen("/splash")
    object LoginScreen: Screen("/login")
    object RegisterScreen: Screen("/register")
    object ResetPasswordFromLink : Screen("/resetPassword")
    object HomeScreen: Screen("/home")
    object SearchScreen: Screen("/search")
    object RestaurantDetailsScreen: Screen("/restaurant")
    object ProfileScreen: Screen("/users")
    object EditProfileScreen: Screen("/editProfile")
}