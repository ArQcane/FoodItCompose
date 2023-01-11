package com.example.data.review.remote.dto

data class UpdateReviewDto(
    val idrestaurant: Int? = null,
    val iduser: Int? = null,
    val review: String? = null,
    val rating: Int? = null,
)
