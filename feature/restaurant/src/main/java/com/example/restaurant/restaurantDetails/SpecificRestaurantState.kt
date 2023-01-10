package com.example.restaurant.restaurantDetails

import com.example.domain.restaurant.TransformedRestaurantAndReview

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
    val isAnimationDone: Boolean = false,
    val review: String = "",
    val rating: Int = 0,
    val reviewError: String? = null,
    val ratingError: String? = null,
    val isUpdated: Boolean = false,
    val isSubmitting: Boolean = false,
)