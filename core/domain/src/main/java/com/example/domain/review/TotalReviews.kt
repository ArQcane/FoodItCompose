package com.example.domain.review

import com.google.gson.annotations.SerializedName

data class TotalReviews(
    @SerializedName("COUNT(review)") val countofreviews: Int,
)
