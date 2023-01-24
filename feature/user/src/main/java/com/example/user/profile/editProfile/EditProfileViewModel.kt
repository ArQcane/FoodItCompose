package com.example.user.profile.editProfile

import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.EditUserInformationUsecase
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser,
    private val editProfileInformationUsecase: EditUserInformationUsecase,
) : ViewModel() {
    private val _editProfileState = MutableStateFlow(EditProfileState())
    val editProfileState = _editProfileState.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    init {
        getCurrentLoggedInUser()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.OnFirstNameChange -> {
                _editProfileState.update { state ->
                    state.copy(
                        first_name = event.first_name,
                        firstNameError = null
                    )
                }
            }
            is EditProfileEvent.OnLastNameChange -> {
                _editProfileState.update { state ->
                    state.copy(
                        last_name = event.last_name,
                        lastNameError = null
                    )
                }
            }
            is EditProfileEvent.OnMobileNumberChange -> {
                _editProfileState.update { state ->
                    state.copy(
                        mobile_number = event.mobile_number,
                        mobileNumberError = null
                    )
                }
            }
            is EditProfileEvent.OnAddressChange -> {
                _editProfileState.update { state ->
                    state.copy(
                        address = event.address,
                        addressError = null
                    )
                }
            }
            is EditProfileEvent.OnProfilePicChange -> {
                _editProfileState.update { state ->
                    state.copy(
                        profile_pic = event.profile_pic,
                        profilePicError = null
                    )
                }
            }
            is EditProfileEvent.OnSubmit -> editProfile()
        }
    }

    private fun editProfile() {
        val first_name = _editProfileState.value.first_name
        val last_name = _editProfileState.value.last_name
        val mobile_number = _editProfileState.value.mobile_number
        val address = _editProfileState.value.address
        val profile_pic = _editProfileState.value.profile_pic
        editProfileInformationUsecase(
            user_id = _editProfileState.value.user_id.toString(),
            first_name = first_name,
            last_name = last_name,
            mobile_number = mobile_number,
            address = address,
            profile_pic = profile_pic
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _editProfileState.update { state ->
                    Log.d("Success", "success!")
                    state.copy(
                        isLoading = false,
                        isUpdated = true
                    )
                }
                is Resource.Failure -> {
                    when (result.error) {
                        is ResourceError.Field -> handleFieldError(
                            result.error as ResourceError.Field
                        )
                        is ResourceError.Default -> handleDefaultError(
                            result.error as ResourceError.Default
                        )
                    }
                }
                is Resource.Loading -> _editProfileState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun getCurrentLoggedInUser() {
        getCurrentLoggedInUserUseCase(
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _editProfileState.update { state ->
                    Log.d("stateUser", state.user_id.toString())
                    val cleanImage: String =
                        result.result.profile_pic?.replace("data:image/png;base64,", "")
                            ?.replace("data:image/jpeg;base64,", "") ?: result.result.profile_pic!!
                    val decodedString: ByteArray = Base64.getDecoder().decode(cleanImage)
                    val decodedByte =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    state.copy(
                        user_id = result.result.user_id,
                        first_name = result.result.first_name,
                        last_name = result.result.last_name,
                        username = result.result.username,
                        gender = result.result.gender,
                        mobile_number = result.result.mobile_number,
                        email = result.result.email,
                        address = result.result.address,
                        profile_pic = decodedByte,
                        isLoading = false,
                    )
                }
                is Resource.Failure -> {
                    _editProfileState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (result.error !is ResourceError.Default) return@onEach
                    val defaultError = result.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _editProfileState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private suspend fun handleDefaultError(defaultError: ResourceError.Default) {
        _errorChannel.send(defaultError.error)
        _editProfileState.update {
            it.copy(isLoading = false)
        }
    }

    private fun handleFieldError(fieldError: ResourceError.Field) {
        _editProfileState.update { state ->
            state.copy(
                isLoading = false,
                firstNameError = fieldError.errors.find { it.field == "first_name" }?.error,
                lastNameError = fieldError.errors.find { it.field == "last_name" }?.error,
                mobileNumberError = fieldError.errors.find { it.field == "mobile_number" }?.error,
                addressError = fieldError.errors.find { it.field == "address" }?.error,
                profilePicError = fieldError.errors.find { it.field == "profile_pic" }?.error,
            )
        }
    }
}