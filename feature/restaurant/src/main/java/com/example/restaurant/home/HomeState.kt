package com.example.restaurant.home

import com.example.domain.restaurant.TransformedRestaurant

data class HomeState(
    val restaurantList: List<TransformedRestaurant> = emptyList(),
    val isLoading: Boolean = true,
)