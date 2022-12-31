package com.example.fooditcompose.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.restaurant.usecases.GetAllRestaurantsUseCase
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getAllRestaurantsUseCase: GetAllRestaurantsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    init {
        getRestaurants()
    }

    private fun getRestaurants() {
        getAllRestaurantsUseCase().onEach {
            when (it) {
                is Resource.Success -> _state.update { state ->
                    state.copy(
                        restaurantList = it.result,
                        isLoading = false
                    )
                }
                is Resource.Failure -> {
                    _state.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (it.error !is ResourceError.Default) return@onEach
                    val defaultError = it.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _state.update { state ->
                    state.copy(isLoading = it.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }


    fun logout() {
        userRepository.deleteToken()
    }
}