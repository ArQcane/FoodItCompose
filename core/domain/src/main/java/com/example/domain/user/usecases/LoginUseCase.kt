package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.ValidateEmail
import com.example.domain.validation.usecases.ValidatePassword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validatePassword: ValidatePassword
) {
    operator fun invoke(
        username: String,
        user_pass: String
    ): Flow<Resource<Unit>> = flow {
        val passwordValidationError = validatePassword(
            value = user_pass,
            flag = ValidatePassword.LOGIN_FLAG
        )
        if (passwordValidationError != null) {
            val error = Resource.Failure<Unit>(
                ResourceError.Field(
                    message = "Invalid fields provided",
                    errors = listOfNotNull(
                        passwordValidationError
                    )
                )
            )
            return@flow emit(error)
        }
        emit(
            Resource.Loading<Unit>(
                isLoading = true
            )
        )
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