package com.example.data.review

import com.example.data.review.remote.RemoteReviewDao
import com.example.data.review.remote.dto.CreateReviewDto
import com.example.data.review.remote.dto.UpdateReviewDto
import com.example.domain.review.Review
import com.example.domain.review.ReviewRepository
import com.example.domain.review.TotalReviews
import com.example.domain.utils.Resource
import javax.inject.Inject


class ReviewRepositoryImpl @Inject constructor(
    private val remoteReviewDao: RemoteReviewDao
) : ReviewRepository {
    override suspend fun getAllReviews(): Resource<List<com.example.domain.review.Review>> =
        remoteReviewDao.getAllReviews()

    override suspend fun getReviewsByUser(userId: String): Resource<List<com.example.domain.review.Review>> =
        remoteReviewDao.getReviewsByUser(userId)

    override suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<com.example.domain.review.Review>> =
        remoteReviewDao.getReviewsByRestaurant(restaurantId)

    override suspend fun getTotalReviewsByUser(userId: String): Resource<TotalReviews> =
        remoteReviewDao.getTotalCountReviewsByUser(userId)


    override suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        idRestaurant: Int?,
        idUser: Int?,
        review: String,
        rating: Int
    ): Resource<String> =
        remoteReviewDao.createReview(
            userId = userId,
            restaurantId = restaurantId,
            CreateReviewDto = CreateReviewDto(
                idRestaurant = idRestaurant,
                idUser = idUser,
                review = review,
                rating = rating
            )
    )

    override suspend fun updateReview(
        userId: Int,
        restaurantId: Int,
        reviewId: String,
        idRestaurant: Int?,
        idUser: Int?,
        review: String?,
        rating: Int?
    ): Resource<com.example.domain.review.Review> =
        remoteReviewDao.updateReview(
            userId = userId,
            restaurantId = restaurantId,
            reviewId = reviewId,
            UpdateReviewDto = UpdateReviewDto(
                idRestaurant = idRestaurant,
                idUser = idUser,
                review = review,
                rating = rating
            )
        )

    override suspend fun deleteReview(reviewId: String): Resource<String> =
        remoteReviewDao.deleteReview(reviewId)
}