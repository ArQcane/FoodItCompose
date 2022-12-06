package com.example.fooditcompose.data.review.remote

import com.example.fooditcompose.data.common.OkHttpDao
import com.example.fooditcompose.data.review.remote.dto.CreateReviewDto
import com.example.fooditcompose.data.review.remote.dto.UpdateReviewDto
import com.example.fooditcompose.domain.review.Review
import com.example.fooditcompose.domain.utils.Resource
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.w3c.dom.Comment


abstract class RemoteReviewDao(
    okHttpClient: OkHttpClient,
    gson: Gson
): OkHttpDao(okHttpClient, gson, "/reviews") {
    abstract suspend fun getAllReviews(): Resource<List<Review>>
    abstract suspend fun getReviewsByUser(userId: String): Resource<List<Review>>
    abstract suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>>
    abstract suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        createReviewDto: CreateReviewDto
    ): Resource<String>

    abstract suspend fun updateReview(
        userId: Int,
        restaurantId: Int,
        reviewId: String,
        updateReviewDto: UpdateReviewDto
    ): Resource<Review>

    abstract suspend fun deleteReview(reviewId: String): Resource<String>
}