package com.example.fooditcompose.data.review.remote

import com.example.fooditcompose.data.review.remote.dto.CreateReviewDto
import com.example.fooditcompose.data.review.remote.dto.UpdateReviewDto
import com.example.fooditcompose.domain.review.Review
import com.example.fooditcompose.domain.utils.Resource

interface RemoteReviewDao {
    suspend fun getAllReviews(): Resource<List<Review>>

    suspend fun getReviewsByUser(userId: String): Resource<List<Review>>

    suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>>

    suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        CreateReviewDto: CreateReviewDto
    ): Resource<String>

    suspend fun updateReview(
        userId: Int,
        restaurantId: Int,
        reviewId: String,
        UpdateReviewDto: UpdateReviewDto
    ): Resource<Review>

    suspend fun deleteReview(reviewId: String): Resource<String>
}