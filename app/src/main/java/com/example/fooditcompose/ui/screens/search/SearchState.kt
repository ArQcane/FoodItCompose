package com.example.fooditcompose.ui.screens.search

import com.example.domain.restaurant.TransformedRestaurant

data class SearchState(
    val searchedQuery: String = "",
    val searchedQueryError: String? = null,
    val searchedRestaurantList: List<TransformedRestaurant> = emptyList(),
    val isLoading: Boolean = false,
)