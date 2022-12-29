package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.ValidateEmail
import com.example.domain.validation.usecases.ValidateMobileNumber
import com.example.domain.validation.usecases.ValidatePassword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateMobileNumber: ValidateMobileNumber
) {
    operator fun invoke(
        first_name: String,
        last_name: String,
        username: String,
        user_pass: String,
        gender: String,
        mobile_number: Int,
        email: String,
        address: String,
        profile_pic: String? = null,
    ): Flow<Resource<Unit>> = flow {
        val emailValidationError = validateEmail(
            value = email
        )
        val passwordValidationError = validatePassword(
            value = user_pass,
            flag = ValidatePassword.CREATE_FLAG
        )
        val mobileNumberValidationError = validateMobileNumber(
            value = mobile_number
        )
        if (emailValidationError != null || passwordValidationError != null || mobileNumberValidationError != null) {
            val error = Resource.Failure<Unit>(
                ResourceError.Field(
                    message = "Invalid fields provided",
                    errors = listOfNotNull(
                        emailValidationError,
                        passwordValidationError,
                        mobileNumberValidationError
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
        val registerResult = userRepository.register(
            first_name = first_name,
            last_name = last_name,
            username = username,
            user_pass = user_pass,
            email = email,
            mobile_number = mobile_number,
            gender = gender,
            address = address,
            profile_pic = profile_pic
        )
        when (registerResult) {
            is Resource.Success -> {
//                userRepository.saveToken(registerResult.result)
                emit(Resource.Success(Unit))
            }
            is Resource.Failure -> {
                emit(Resource.Failure(registerResult.error))
            }
            else -> Unit
        }
    }
}