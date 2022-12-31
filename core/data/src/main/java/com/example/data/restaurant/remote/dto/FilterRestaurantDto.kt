package com.example.data.restaurant.remote.dto

data class FilterRestaurantDto(
    val region: String? = null,
    val cuisine: String? = null,
    val average_price_range: Int? = null,
    val average_rating: Int? = null
)