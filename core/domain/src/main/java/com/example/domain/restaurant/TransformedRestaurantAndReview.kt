package com.example.domain.restaurant

import com.example.domain.review.Review
import com.example.domain.review.TransformedReview

data class TransformedRestaurantAndReview(
    val id: Int,
    val name: String,
    val avg_price: Float,
    val cuisine: String,
    val biography: String,
    val opening_hours: String,
    val region: String,
    val restaurant_logo: String,
    val location_lat: Float,
    val location_long: Float,
    val location: String,
    val restaurant_banner: String,
    val reviews: List<TransformedReview>,
    val isFavouriteByCurrentUser: Boolean,
    val averageRating: Double,
    val ratingCount: Int,
)