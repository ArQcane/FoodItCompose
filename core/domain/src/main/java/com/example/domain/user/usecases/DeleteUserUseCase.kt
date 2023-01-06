package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.ValidatePassword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
){
    operator fun invoke(
        user_id: String
    ): Flow<Resource<Unit>> = flow {
        emit(
            Resource.Loading<Unit>(
                isLoading = true
            )
        )
        val deleteResult = userRepository.deleteAccount(
            user_id
        )
        when (deleteResult) {
            is Resource.Success -> {
                emit(Resource.Success(Unit))
            }
            is Resource.Failure -> {
                emit(Resource.Failure(deleteResult.error))
            }
            else -> Unit
        }
    }
}