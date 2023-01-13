package com.example.restaurant.home

import com.example.domain.restaurant.TransformedRestaurant
import com.google.android.gms.maps.model.LatLng

data class HomeState(
    val restaurantList: List<TransformedRestaurant> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val currentUserUsername: String? = null,
    val favRestaurants: List<Int> = emptyList(),
    val featuredRestaurants: List<Int> = emptyList(),
    val currentLocation: LatLng? = null,
)