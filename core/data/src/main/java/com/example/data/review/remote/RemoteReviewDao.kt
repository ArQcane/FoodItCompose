package com.example.data.review.remote

import com.example.data.review.remote.dto.CreateReviewDto
import com.example.data.review.remote.dto.UpdateReviewDto
import com.example.domain.review.Review
import com.example.domain.review.TotalReviews
import com.example.domain.review.TransformedReview
import com.example.domain.utils.Resource
interface RemoteReviewDao {
    suspend fun getAllReviews(): Resource<List<Review>>

    suspend fun getReviewsByUser(userId: String): Resource<List<Review>>

    suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>>

    suspend fun getReviewById(reviewId: String): Resource<Review>

    suspend fun getTotalCountReviewsByUser(userId: String): Resource<TotalReviews>

    suspend fun createReview(
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