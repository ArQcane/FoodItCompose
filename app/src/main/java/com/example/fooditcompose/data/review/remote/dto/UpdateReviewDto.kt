package com.example.fooditcompose.data.review.remote.dto

data class UpdateReviewDto(
    val idRestaurant: Int? = null,
    val idUser: Int? = null,
    val review: String? = null,
    val rating: Int? = null,
)
