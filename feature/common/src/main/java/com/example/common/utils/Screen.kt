package com.example.common.utils

sealed class Screen (val route: String){
    object HomeScreen: Screen("/home")
    object SearchScreen: Screen("/search")
    object SpecificRestaurantScreen: Screen("restaurant/{restaurantId}")
    object RestaurantDetailsScreen: Screen("/restaurant")
    object ProfileScreen: Screen("/users")
    object EditProfileScreen: Screen("/editProfile")
}