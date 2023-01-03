package com.example.restaurant.individualScreen

import com.example.domain.restaurant.TransformedRestaurant
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.domain.user.ReviewUser
import com.example.domain.user.User

data class SpecificRestaurantState(
    val transformedRestaurant: TransformedRestaurantAndReview = TransformedRestaurantAndReview(
        id = 0,
        name = "",
        biography = "",
        isFavouriteByCurrentUser = false,
        averageRating = 0.0,
        avg_price = 0.0F,
        cuisine = "",
        location = "",
        location_lat = 0F,
        location_long = 0F,
        opening_hours = "",
        ratingCount = 0,
        region = "",
        restaurant_banner = "",
        restaurant_logo = "",
        reviews = emptyList(),
    ),
    val isLoading: Boolean = true,
)