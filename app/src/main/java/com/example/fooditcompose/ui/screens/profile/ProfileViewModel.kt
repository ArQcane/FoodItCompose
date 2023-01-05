package com.example.fooditcompose.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.review.usecases.GetTotalReviewsMadeByUserUseCase
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.restaurant.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getTotalReviewsMadeByUserUseCase: GetTotalReviewsMadeByUserUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser,
): ViewModel() {
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
                        user = result.result
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

}