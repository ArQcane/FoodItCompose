package com.example.data.review.remote.dto

data class CreateReviewDto(
    val idrestaurant: Int,
    val iduser: Int,
    val review: String,
    val rating: Int,
)
