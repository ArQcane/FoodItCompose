package com.example.fooditcompose.domain.restaurant

data class Restaurant(
    val restaurant_id: Int,
    val restaurant_name: String,
    val  average_price_range: Float,
    val average_rating: Float,
    val cuisine: String,
    val biography: String,
    val opening_hours: String,
    val region: String,
    val restaurant_logo: String,
    val location_lat: Float,
    val location_long: Float,
    val location: String,
    val restaurant_banner: String,
)
