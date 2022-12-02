package com.example.fooditcompose.data.review

import java.time.LocalDateTime

data class Review(
    val review_id: Int,
    val idrestaurant: Int,
    val iduser: Int,
    val review: String,
    val rating: Double,
    val dateposted: LocalDateTime,

)
