package com.example.domain.review

import com.example.domain.user.ReviewUser
import com.google.gson.annotations.SerializedName
import java.util.*

data class TransformedReview(
    val review_id: Int,
    val idrestaurant: Int,
    val iduser: Int,
    val review: String,
    val rating: Double,
    val user: ReviewUser,
    @SerializedName("dateposted") val dateposted: Date,
)