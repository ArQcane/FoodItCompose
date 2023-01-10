package com.example.domain.review.usecases

import android.util.Log
import com.example.domain.review.ReviewRepository
import com.example.domain.review.TotalReviews
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class GetTotalReviewsMadeByUserUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser
) {
    operator fun invoke(
    ): Flow<Resource<TotalReviews>> = flow {
        emit(Resource.Loading(isLoading = true))
        val userId = getUserId() ?: return@flow emit(
            Resource.Failure(
                ResourceError.Default("Must be logged in to do this action")
            )
        )
        Log.d("userId", userId)
        val totalReviews = reviewRepository.getTotalReviewsByUser(userId)
        if (totalReviews !is Resource.Success) return@flow emit(
            Resource.Failure(
                (totalReviews as Resource.Failure).error
            )
        )
        Log.d("totalReviews", totalReviews.result.toString())
        emit(Resource.Success(totalReviews.result))
    }
    private suspend fun getUserId(): String? {
        val userResource = getCurrentLoggedInUserUseCase().last()
        if (userResource is Resource.Failure) return null
        return (userResource as Resource.Success).result.user_id.toString()
    }

}