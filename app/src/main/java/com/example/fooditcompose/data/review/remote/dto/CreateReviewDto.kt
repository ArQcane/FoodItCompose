package com.example.fooditcompose.data.review.remote.dto

import com.example.fooditcompose.domain.restaurant.Restaurant

data class CreateReviewDto(
    val idRestaurant: Int? = null,
    val idUser: Int? = null,
    val review: String,
    val rating: Int,
)
