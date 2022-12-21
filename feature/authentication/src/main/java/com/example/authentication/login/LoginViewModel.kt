package com.example.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.usecases.LoginUseCase
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnUsernameChange -> {
                _loginState.update { state ->
                    state.copy(
                        username = event.username,
                        usernameError = null
                    )
                }
            }
            is LoginEvent.OnUserPassChange -> {
                _loginState.update { state ->
                    state.copy(
                        user_pass = event.user_pass,
                        userPassError = null
                    )
                }
            }
            is LoginEvent.OnSubmit -> login()
        }
    }

    private fun login() {
        val username = _loginState.value.username
        val user_pass = _loginState.value.user_pass
        loginUseCase(
            username = username,
            user_pass = user_pass,
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _loginState.update { state ->
                    state.copy(
                        isLoading = false,
                        isLoggedIn = true
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
                is Resource.Loading -> _loginState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun handleDefaultError(defaultError: ResourceError.Default) {
        _errorChannel.send(defaultError.error)
        _loginState.update {
            it.copy(isLoading = false)
        }
    }

    private fun handleFieldError(fieldError: ResourceError.Field) {
        _loginState.update { state ->
            state.copy(
                isLoading = false,
                usernameError = fieldError.errors.find { it.field == "username" }?.error,
                userPassError = fieldError.errors.find { it.field == "user_pass" }?.error
            )
        }
    }
}