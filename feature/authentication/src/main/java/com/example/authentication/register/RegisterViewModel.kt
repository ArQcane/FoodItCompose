package com.example.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.login.LoginEvent
import com.example.authentication.login.LoginState
import com.example.domain.user.usecases.CreateAccountUseCase
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase
): ViewModel() {
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnFirstNameChange -> {
                _registerState.update { state ->
                    state.copy(
                        first_name = event.first_name,
                        firstNameError = null
                    )
                }
            }
            is RegisterEvent.OnLastNameChange -> {
                _registerState.update { state ->
                    state.copy(
                        last_name = event.last_name,
                        lastNameError = null
                    )
                }
            }
            is RegisterEvent.OnUsernameChange -> {
                _registerState.update { state ->
                    state.copy(
                        username = event.username,
                        usernameError = null
                    )
                }
            }
            is RegisterEvent.OnUserPassChange -> {
                _registerState.update { state ->
                    state.copy(
                        user_pass = event.user_pass,
                        userPassError = null
                    )
                }
            }
            is RegisterEvent.OnConfirmUserPassChange -> {
                _registerState.update { state ->
                    state.copy(
                        confirmUserPass = event.confirmUserPass,
                        confirmUserPassError = null
                    )
                }
            }
            is RegisterEvent.OnGenderChange -> {
                _registerState.update { state ->
                    state.copy(
                        gender = event.gender,
                        genderError = null
                    )
                }
            }
            is RegisterEvent.OnMobileNumberChange -> {
                _registerState.update { state ->
                    state.copy(
                        mobile_number = event.mobile_number.toString(),
                        mobileNumberError = null
                    )
                }
            }
            is RegisterEvent.OnEmailChange -> {
                _registerState.update { state ->
                    state.copy(
                        email = event.email,
                        emailError = null
                    )
                }
            }
            is RegisterEvent.OnAddressChange -> {
                _registerState.update { state ->
                    state.copy(
                        address = event.address,
                        addressError = null
                    )
                }
            }
            is RegisterEvent.OnProfilePicChange -> {
                _registerState.update { state ->
                    state.copy(
                        profile_pic = event.profile_pic,
                        profilePicError = null
                    )
                }
            }
            is RegisterEvent.OnSubmit -> register()
        }
    }
    private fun register() {
        val first_name = _registerState.value.first_name
        val last_name = _registerState.value.last_name
        val username = _registerState.value.username
        val user_pass = _registerState.value.user_pass
        val gender = _registerState.value.gender
        val mobile_number = _registerState.value.mobile_number.toInt()
        val email = _registerState.value.email
        val address = _registerState.value.address
        val profile_pic = _registerState.value.profile_pic
        createAccountUseCase(
            first_name = first_name,
            last_name = last_name,
            username = username,
            user_pass = user_pass,
            gender = gender,
            mobile_number = mobile_number,
            email = email,
            address = address,
            profile_pic = profile_pic,
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _registerState.update { state ->
                    state.copy(
                        isLoading = false,
                        isCreated = true
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
                is Resource.Loading -> _registerState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }
    private suspend fun handleDefaultError(defaultError: ResourceError.Default) {
        _errorChannel.send(defaultError.error)
        _registerState.update {
            it.copy(isLoading = false)
        }
    }

    private fun handleFieldError(fieldError: ResourceError.Field) {
        _registerState.update { state ->
            state.copy(
                isLoading = false,
                firstNameError = fieldError.errors.find { it.field == "first_name" }?.error,
                lastNameError = fieldError.errors.find { it.field == "last_name" }?.error,
                usernameError = fieldError.errors.find { it.field == "username" }?.error,
                userPassError = fieldError.errors.find { it.field == "user_pass" }?.error,
                genderError = fieldError.errors.find { it.field == "gender" }?.error,
                mobileNumberError = fieldError.errors.find { it.field == "mobile_number" }?.error,
                emailError = fieldError.errors.find { it.field == "email" }?.error,
                addressError = fieldError.errors.find { it.field == "address" }?.error,
                profilePicError = fieldError.errors.find { it.field == "profile_pic" }?.error,
            )
        }
    }

}