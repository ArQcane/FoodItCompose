package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateMobileNumber: ValidateMobileNumber,
    private val validateConfirmPassword : ValidateConfirmPassword,
    private val validateUsername: ValidateUsername,
    private val validateFirstName: ValidateFirstName,
    private val validateLastName: ValidateLastName,
    private val validateUserAddress: ValidateAddress,
    private val validateGender: ValidateGender,
) {
    operator fun invoke(
        first_name: String,
        last_name: String,
        username: String,
        user_pass: String,
        confirmUserPass: String,
        gender: String,
        mobile_number: Long,
        email: String,
        address: String,
        profile_pic: String? = null,
    ): Flow<Resource<Unit>> = flow {
        when (val error = validateFields(first_name, last_name, gender, mobile_number, address, username, email, user_pass, confirmUserPass)) {
            is Resource.Failure -> return@flow emit(error)
            else -> emit(Resource.Loading(isLoading = true))
        }
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
    private fun validateFields(
        first_name: String,
        last_name: String,
        gender: String,
        mobile_number: Long,
        address: String,
        username: String,
        email: String,
        user_pass: String,
        confirmUserPassword: String
    ): Resource<Unit> {
        val usernameValidationError = validateUsername(
            value = username
        )
        val firstNameValidationError = validateFirstName(
            value = first_name
        )
        val lastNameValidationError = validateLastName(
            value = last_name
        )
        val mobileNumberValidationError = validateMobileNumber(
            value = mobile_number
        )
        val genderValidationError = validateGender(
            value = gender
        )
        val addressValidationError = validateUserAddress(
            value = address
        )
        val emailValidationError = validateEmail(
            value = email
        )
        val passwordValidationError = validatePassword(
            value = user_pass,
            flag = ValidatePassword.CREATE_FLAG
        )
        val confirmPasswordValidationError = validateConfirmPassword(
            value = confirmUserPassword,
            user_pass = user_pass
        )
        if (
            emailValidationError != null ||
            passwordValidationError != null ||
            usernameValidationError != null ||
            firstNameValidationError != null ||
            lastNameValidationError != null ||
            mobileNumberValidationError != null ||
            genderValidationError != null ||
            addressValidationError != null ||
            confirmPasswordValidationError != null
        ) {
            return Resource.Failure(
                ResourceError.Field(
                    message = "Invalid fields provided",
                    errors = listOfNotNull(
                        firstNameValidationError,
                        lastNameValidationError,
                        genderValidationError,
                        mobileNumberValidationError,
                        addressValidationError,
                        usernameValidationError,
                        emailValidationError,
                        passwordValidationError,
                        confirmPasswordValidationError
                    )
                )
            )
        }
        return Resource.Success(Unit)
    }
}