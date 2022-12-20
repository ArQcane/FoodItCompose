package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        username: String,
        user_pass: String
    ): Flow<Resource<Unit>> = flow {
        emit(
            Resource.Loading<Unit>(
                isLoading = true
            ))
        val loginResult = userRepository.login(
            username = username,
            user_pass = user_pass
        )
        when (loginResult) {
            is Resource.Success -> {
                userRepository.saveToken(loginResult.result)
                emit(Resource.Success(Unit))
            }
            is Resource.Failure -> {
                emit(Resource.Failure(loginResult.error))
            }
            else -> Unit
        }
    }
}