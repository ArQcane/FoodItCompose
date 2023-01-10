package com.example.domain.user.usecases

import com.example.domain.user.ReviewUser
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        user_id: String,
    ): Flow<Resource<ReviewUser>> = flow {
        emit(Resource.Loading(isLoading = true))
        val userResource = userRepository.getUserById(user_id)
        if (userResource !is Resource.Success) return@flow emit(
            Resource.Failure(
                (userResource as Resource.Failure).error
            )
        )
        emit(Resource.Success(userResource.result))
    }
}