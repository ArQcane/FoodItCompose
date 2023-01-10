package com.example.domain.user.usecases

import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Base64.encodeToString
import androidx.annotation.RequiresApi
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
    private val validateProfilePic: ValidateProfilePic,
    private val validateGender: ValidateGender,
) {
    @RequiresApi(Build.VERSION_CODES.O)
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
        profile_pic: Bitmap? = null,
    ): Flow<Resource<Unit>> = flow {
        when (val error = validateFields(first_name, last_name, gender, mobile_number, address, username, email, user_pass, confirmUserPass, profile_pic)) {
            is Resource.Failure -> return@flow emit(error)
            else -> emit(Resource.Loading(isLoading = true))
        }
        val byteArrayProfilePic = convertBitmapToByteArray(profile_pic!!)
        val base64ProfilePic = encodeToString(byteArrayProfilePic, Base64.DEFAULT).replace("\n", "")
        val registerResult = userRepository.register(
            first_name = first_name,
            last_name = last_name,
            username = username,
            user_pass = user_pass,
            email = email,
            mobile_number = mobile_number,
            gender = gender,
            address = address,
            profile_pic = base64ProfilePic
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
        gender: String,
        mobile_number: Long,
        address: String,
        username: String,
        email: String,
        user_pass: String,
        confirmUserPassword: String,
        profile_pic: Bitmap?,
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
        val profilePicValidationError = validateProfilePic(
            value = profile_pic
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
            confirmPasswordValidationError != null ||
            profilePicValidationError != null
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
                        confirmPasswordValidationError,
                        profilePicValidationError
                    )
                )
            )
        }
        return Resource.Success(Unit)
    }
}