package com.example.restaurant.restaurantDetails.utils

import androidx.compose.runtime.Composable


sealed class TabItem(val title: String, val composable: @Composable () -> Unit) {
    class MoreRestaurantDetailScreen(
        composable: @Composable () -> Unit
    ) : TabItem(
        title = "Details",
        composable = composable
    )

    class Reviews(
        composable: @Composable () -> Unit
    ) : TabItem(
        title = "Reviews",
        composable = composable
    )
}