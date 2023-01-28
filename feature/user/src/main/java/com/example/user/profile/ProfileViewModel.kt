package com.example.user.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.review.usecases.GetTotalReviewsMadeByUserUseCase
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.DeleteUserUseCase
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.user.usecases.ResetPasswordUseCase
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getTotalReviewsMadeByUserUseCase: GetTotalReviewsMadeByUserUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    fun logout() {
        userRepository.deleteToken()
    }

    init {
        getCurrentLoggedInUser()
        getTotalReviewsByUser()
    }

    private fun getCurrentLoggedInUser() {
        getCurrentLoggedInUserUseCase(
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _profileState.update { state ->
                    Log.d("stateUser", state.user.user_id.toString())
                    state.copy(
                        isLoading = false,
                        user = result.result,
                    )
                }
                is Resource.Failure -> {
                    _profileState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (result.error !is ResourceError.Default) return@onEach
                    val defaultError = result.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _profileState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun getTotalReviewsByUser() {
        getTotalReviewsMadeByUserUseCase(
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _profileState.update { state ->
                    state.copy(
                        isLoading = false,
                        totalReviews = result.result.countofreviews
                    )
                }
                is Resource.Failure -> {
                    _profileState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (result.error !is ResourceError.Default) return@onEach
                    val defaultError = result.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _profileState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun deleteUser() {
        val user_id = _profileState.value.user.user_id
        deleteUserUseCase(
            user_id = user_id.toString()
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _profileState.update { state ->
                    userRepository.deleteToken()
                    state.copy(
                        isLoading = false,
                    )
                }
                is Resource.Failure -> {
                    _profileState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (result.error !is ResourceError.Default) return@onEach
                    val defaultError = result.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _profileState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun resetPassword(email: String) {
        resetPasswordUseCase(
            email = email
        ).onEach { result ->
            when (result) {
                is Resource.Success -> _profileState.update { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
                is Resource.Failure -> {
                    _profileState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (result.error !is ResourceError.Default) return@onEach
                    val defaultError = result.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _profileState.update { state ->
                    state.copy(isLoading = result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }
}