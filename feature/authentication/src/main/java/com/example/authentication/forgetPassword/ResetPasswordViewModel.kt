package com.example.authentication.forgetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.usecases.ResetPasswordUseCase
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _resetPasswordState = MutableStateFlow(ResetPasswordState())
    val resetPasswordState = _resetPasswordState.asStateFlow()

    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.OnEmailChange -> {
                _resetPasswordState.update { state ->
                    state.copy(
                        email = event.email,
                        emailError = null
                    )
                }
            }
            is ResetPasswordEvent.OnSubmit -> resetPassword()
        }
    }

    private fun resetPassword(){
        val email = _resetPasswordState.value.email
        resetPasswordUseCase(
            email = email
        ).onEach { result ->
            when(result){
                is Resource.Success ->_resetPasswordState.update{ state->
                    state.copy(
                        isLoading = false,
                        isSent = true,
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
                is Resource.Loading -> _resetPasswordState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }
    private suspend fun handleDefaultError(defaultError: ResourceError.Default) {
        _errorChannel.send(defaultError.error)
        _resetPasswordState.update {
            it.copy(isLoading = false)
        }
    }

    private fun handleFieldError(fieldError: ResourceError.Field) {
        _resetPasswordState.update { state ->
            state.copy(
                isLoading = false,
                emailError = fieldError.errors.find { it.field == "email" }?.error,
            )
        }
    }


}