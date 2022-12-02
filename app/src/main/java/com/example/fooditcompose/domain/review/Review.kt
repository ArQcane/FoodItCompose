package com.example.fooditcompose.domain.review

import com.google.gson.annotations.SerializedName
import java.util.*

data class Review(
    val review_id: Int,
    val idrestaurant: Int,
    val iduser: Int,
    val review: String,
    val rating: Double,
    @SerializedName("dateposted") val dateposted: Date,

    )