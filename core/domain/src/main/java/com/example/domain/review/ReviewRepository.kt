package com.example.domain.review

import com.example.domain.utils.Resource

interface ReviewRepository {
    suspend fun getAllReviews(): Resource<List<Review>>

    suspend fun getReviewsByUser(userId: String): Resource<List<Review>>

    suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>>

    suspend fun getReviewsById(reviewId: String): Resource<Review>

    suspend fun getTotalReviewsByUser(userId: String): Resource<TotalReviews>

    suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        review: String,
        rating: Int,
    ): Resource<String>

    suspend fun updateReview(
        reviewId: String,
        idRestaurant: Int?,
        idUser: Int?,
        review: String?,
        rating: Int?
    ): Resource<Review>

    suspend fun deleteReview(reviewId: String): Resource<String>
}