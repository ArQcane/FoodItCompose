package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EditUserInformationUsecase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateMobileNumber: ValidateMobileNumber,
    private val validateFirstName: ValidateFirstName,
    private val validateLastName: ValidateLastName,
    private val validateUserAddress: ValidateAddress,
) {
    operator fun invoke(
        user_id: String,
        first_name: String,
        last_name: String,
        mobile_number: Long,
        address: String,
        profile_pic: String? = null,
    ): Flow<Resource<Unit>> = flow {
        when (val error = validateFields(first_name, last_name, address, mobile_number)) {
            is Resource.Failure -> return@flow emit(error)
            else -> emit(Resource.Loading(isLoading = true))
        }
        val updateResult = userRepository.updateAccount(
            userId = user_id,
            firstName = first_name,
            lastName = last_name,
            phoneNumber = mobile_number,
            address = address,
            profile_pic = profile_pic
        )
        when (updateResult) {
            is Resource.Success -> {
//                userRepository.saveToken(registerResult.result)
                emit(Resource.Success(Unit))
            }
            is Resource.Failure -> {
                emit(Resource.Failure(updateResult.error))
            }
            else -> Unit
        }
    }
    private fun validateFields(
        first_name: String,
        last_name: String,
        address: String,
        mobile_number: Long,
    ): Resource<Unit> {
        val firstNameValidationError = validateFirstName(
            value = first_name
        )
        val lastNameValidationError = validateLastName(
            value = last_name
        )
        val mobileNumberValidationError = validateMobileNumber(
            value = mobile_number
        )
        val addressValidationError = validateUserAddress(
            value = address
        )
        if (
            firstNameValidationError != null ||
            lastNameValidationError != null ||
            mobileNumberValidationError != null ||
            addressValidationError != null
        ) {
            return Resource.Failure(
                ResourceError.Field(
                    message = "Invalid fields provided",
                    errors = listOfNotNull(
                        firstNameValidationError,
                        lastNameValidationError,
                        mobileNumberValidationError,
                        addressValidationError,
                    )
                )
            )
        }
        return Resource.Success(Unit)
    }
}