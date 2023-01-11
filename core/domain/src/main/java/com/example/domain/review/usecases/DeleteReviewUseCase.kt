package com.example.domain.review.usecases

import com.example.domain.review.ReviewRepository
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(reviewId: String) = flow<Resource<Unit>> {
        emit(Resource.Loading(isLoading = true))
        val tokenResource = userRepository.getToken()
        if (tokenResource !is Resource.Success) {
            when (tokenResource) {
                is Resource.Failure -> {
                    emit(Resource.Failure(tokenResource.error))
                    return@flow
                }
                else -> throw IllegalStateException()
            }
        }
        val deleteResource = reviewRepository.deleteReview(
            reviewId = reviewId
        )
        when(deleteResource) {
            is Resource.Success -> emit(Resource.Success(Unit))
            is Resource.Failure -> emit(Resource.Failure(deleteResource.error))
            else -> throw IllegalStateException()
        }
    }
}