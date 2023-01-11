package com.example.data.review

import com.example.data.review.remote.RemoteReviewDao
import com.example.data.review.remote.dto.CreateReviewDto
import com.example.data.review.remote.dto.UpdateReviewDto
import com.example.domain.review.Review
import com.example.domain.review.ReviewRepository
import com.example.domain.review.TotalReviews
import com.example.domain.review.TransformedReview
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

    override suspend fun getReviewsById(reviewId: String): Resource<Review> =
        remoteReviewDao.getReviewById(reviewId)

    override suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        review: String,
        rating: Int
    ): Resource<String> =
        remoteReviewDao.createReview(
            CreateReviewDto = CreateReviewDto(
                idrestaurant = restaurantId,
                iduser = userId,
                review = review,
                rating = rating
            )
    )

    override suspend fun updateReview(
        reviewId: String,
        idRestaurant: Int?,
        idUser: Int?,
        review: String?,
        rating: Int?
    ): Resource<Review> =
        remoteReviewDao.updateReview(
            reviewId = reviewId,
            UpdateReviewDto = UpdateReviewDto(
                idrestaurant = idRestaurant,
                iduser = idUser,
                review = review,
                rating = rating
            )
        )

    override suspend fun deleteReview(reviewId: String): Resource<String> =
        remoteReviewDao.deleteReview(reviewId)
}