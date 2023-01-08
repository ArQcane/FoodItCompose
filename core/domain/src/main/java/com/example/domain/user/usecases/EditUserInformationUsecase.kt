package com.example.domain.user.usecases

import android.graphics.Bitmap
import android.util.Base64
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class EditUserInformationUsecase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateMobileNumber: ValidateMobileNumber,
    private val validateFirstName: ValidateFirstName,
    private val validateLastName: ValidateLastName,
    private val validateUserAddress: ValidateAddress,
    private val validateProfilePic: ValidateProfilePic,
) {
    operator fun invoke(
        user_id: String,
        first_name: String,
        last_name: String,
        mobile_number: Long,
        address: String,
        profile_pic: Bitmap? = null,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading(isLoading = true))
        when (val error =
            validateFields(first_name, last_name, address, mobile_number, profile_pic)) {
            is Resource.Failure -> return@flow emit(error)
            else -> emit(Resource.Loading(isLoading = true))
        }
        val byteArrayProfilePic = convertBitmapToByteArray(profile_pic!!)
        val base64ProfilePic = Base64.encodeToString(byteArrayProfilePic, Base64.DEFAULT)
            .replace("\n", "")

        val updateResult = userRepository.updateAccount(
            userId = user_id,
            firstName = first_name,
            lastName = last_name,
            phoneNumber = mobile_number,
            address = address,
            profile_pic = base64ProfilePic
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

    private suspend fun convertBitmapToByteArray(
        bitmap: Bitmap
    ): ByteArray = coroutineScope {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        baos.toByteArray().also {
            launch(Dispatchers.IO) { baos.close() }
        }
    }

    private fun validateFields(
        first_name: String,
        last_name: String,
        address: String,
        mobile_number: Long,
        profile_pic: Bitmap?,
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
        val profilePicValidationError = validateProfilePic(
            value = profile_pic
        )
        if (
            firstNameValidationError != null ||
            lastNameValidationError != null ||
            mobileNumberValidationError != null ||
            addressValidationError != null ||
            profilePicValidationError != null
        ) {
            return Resource.Failure(
                ResourceError.Field(
                    message = "Invalid fields provided",
                    errors = listOfNotNull(
                        firstNameValidationError,
                        lastNameValidationError,
                        mobileNumberValidationError,
                        addressValidationError,
                        profilePicValidationError
                    )
                )
            )
        }
        return Resource.Success(Unit)
    }
}