package com.example.domain.review.usecases

import com.example.domain.review.ReviewRepository
import com.example.domain.review.TransformedReview
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser
) {
    operator fun invoke(
        restaurantId: String,
        review: String,
        rating: Int
    ) = flow<Resource<TransformedReview>> {
        emit(Resource.Loading(isLoading = true))
        val userId = getUserId() ?: return@flow emit(
            Resource.Failure(
                ResourceError.Default("Must be logged in to do this action")
            )
        )
        validate(
            review = review,
            rating = rating
        )?.let { return@flow emit(Resource.Failure(it)) }
        val insertIdResource = reviewRepository.createReview(
            userId = userId.toInt(),
            restaurantId = restaurantId.toInt(),
            review = review,
            rating = rating
        )
        when (insertIdResource) {
            is Resource.Failure -> emit(
                Resource.Failure(error = insertIdResource.error)
            )
            is Resource.Success -> {
                val reviewResource = reviewRepository.getReviewsById(
                     reviewId = insertIdResource.result
                )
                if (reviewResource !is Resource.Success) return@flow emit(
                    Resource.Failure((reviewResource as Resource.Failure).error)
                )
                val userObject = userRepository.getUserById(userId)
                if (userObject !is Resource.Success) return@flow emit(
                    Resource.Failure((userObject as Resource.Failure).error)
                )
                val transformedReview = TransformedReview(
                    review_id = reviewResource.result.review_id,
                    idrestaurant = reviewResource.result.idrestaurant,
                    iduser = reviewResource.result.iduser,
                    dateposted = reviewResource.result.dateposted,
                    review = reviewResource.result.review,
                    rating = reviewResource.result.rating,
                    user = userObject.result,
                )
                emit(Resource.Success(transformedReview))
            }
            else -> Unit
        }
    }

    private fun validate(
        review: String,
        rating: Int,
    ): ResourceError.Field? {
        var error = ResourceError.Field(
            message = "Errors in fields provided",
            errors = emptyList()
        )
        if (review.isEmpty()) error = error.copy(
            errors = error.errors.toMutableList().apply {
                add(
                    ResourceError.FieldErrorItem(
                        field = "review",
                        error = "Review required!"
                    )
                )
            }
        )
        if (rating == 0) error = error.copy(
            errors = error.errors.toMutableList().apply {
                add(
                    ResourceError.FieldErrorItem(
                        field = "rating",
                        error = "Rating required!"
                    )
                )
            }
        )
        if (error.errors.isEmpty()) return null
        return error
    }

    private suspend fun getUserId(): String? {
        val userResource = getCurrentLoggedInUserUseCase().last()
        if (userResource is Resource.Failure) return null
        return (userResource as Resource.Success).result.user_id.toString()
    }
}