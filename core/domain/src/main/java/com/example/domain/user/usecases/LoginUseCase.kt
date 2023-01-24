package com.example.domain.user.usecases

import android.graphics.Bitmap
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.ValidatePassword
import com.example.domain.validation.usecases.ValidateUsername
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validatePassword: ValidatePassword,
    private val validateUsername: ValidateUsername
) {
    operator fun invoke(
        username: String,
        user_pass: String
    ): Flow<Resource<Unit>> = flow {
        when (val error = validateFields(username, user_pass)) {
            is Resource.Failure -> return@flow emit(error)
            else -> emit(Resource.Loading(isLoading = true))
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

    private fun validateFields(
        username: String,
        user_pass: String,

    ): Resource<Unit> {
        val usernameValidationError = validateUsername(
            value = username
        )
        val passwordValidationError = validatePassword(
            value = user_pass,
            flag = ValidatePassword.LOGIN_FLAG
        )
        if (
            passwordValidationError != null ||
            usernameValidationError != null
        ) {
            return Resource.Failure(
                ResourceError.Field(
                    message = "Invalid fields provided",
                    errors = listOfNotNull(
                        usernameValidationError,
                        passwordValidationError,
                    )
                )
            )
        }
        return Resource.Success(Unit)
    }
}