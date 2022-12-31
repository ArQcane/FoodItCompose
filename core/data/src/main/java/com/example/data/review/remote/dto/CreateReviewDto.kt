package com.example.data.review.remote.dto

data class CreateReviewDto(
    val idRestaurant: Int? = null,
    val idUser: Int? = null,
    val review: String,
    val rating: Int,
)
