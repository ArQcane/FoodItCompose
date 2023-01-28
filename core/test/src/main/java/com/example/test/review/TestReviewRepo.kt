package com.example.test.review

import com.example.domain.review.Review
import com.example.domain.review.ReviewRepository
import com.example.domain.review.TotalReviews
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import java.util.*

class TestReviewRepo : ReviewRepository {
    var reviews: List<Review> = emptyList()

    init {
        reviews = reviews.toMutableList().apply {
            repeat(10) {
                val review = Review(
                    review_id = it,
                    review = "review $it",
                    rating = (it % 5).toDouble(),
                    idrestaurant = it,
                    iduser = it,
                    dateposted = Date(),
                )
                add(review)
            }
        }
    }

    override suspend fun getAllReviews(): Resource<List<Review>> {
        return Resource.Success(reviews)
    }

    override suspend fun getReviewsByUser(userId: String): Resource<List<Review>> {
        return Resource.Success(
            reviews.filter { it.iduser.toString() == userId }
        )
    }

    override suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>> {
        return Resource.Success(
            reviews.filter { it.idrestaurant.toString() == restaurantId }
        )
    }

    override suspend fun getReviewsById(reviewId: String): Resource<Review> {
        val review = reviews.find { it.review_id.toString() == reviewId } ?: return Resource.Failure(
            ResourceError.Default("Unable to find comment with id $reviewId")
        )
        return Resource.Success(review)
    }

    override suspend fun getTotalReviewsByUser(userId: String): Resource<TotalReviews> {
        return Resource.Success(TotalReviews(0))
    }

    override suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        review: String,
        rating: Int
    ): Resource<String> {
        val index = reviews.size
        val review = Review(
            review_id = index,
            review = review,
            rating = rating.toDouble(),
            idrestaurant = restaurantId,
            iduser = userId,
            dateposted = Date(),
        )
        reviews = reviews.toMutableList().apply {
            add(review)
        }
        return Resource.Success(review.review_id.toString())
    }

    override suspend fun updateReview(
        reviewId: String,
        idRestaurant: Int?,
        idUser: Int?,
        review: String?,
        rating: Int?
    ): Resource<Review> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(reviewId: String): Resource<String> {
        val review = reviews.find { it.review_id.toString() == reviewId } ?: return Resource.Failure(
            ResourceError.Default("Unable to find comment with id $reviewId")
        )
        reviews = reviews.filter { it.review_id != review.review_id }
        return Resource.Success("Successfully deleted comment with id $reviewId")
    }
}