package com.example.restaurant.restaurantDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.favourites.usecases.ToggleFavouritesUseCase
import com.example.domain.restaurant.usecases.GetSpecificRestaurantUseCase
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SpecificRestaurantViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getSpecificRestaurantUseCase: GetSpecificRestaurantUseCase,
    private val getCurrentLoggedInUser: GetCurrentLoggedInUser,
    private val toggleFavouritesUseCase: ToggleFavouritesUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _specificRestaurantState = MutableStateFlow(SpecificRestaurantState())
    val specificRestaurantState = _specificRestaurantState.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    init {
        savedStateHandle.get<String>("restaurantId")?.let {
            getSpecificRestaurant(it)
        }
    }

    private fun getSpecificRestaurant(restaurantId: String) {
        getSpecificRestaurantUseCase(
            restaurantId = restaurantId,
        ).onEach {
            when (it) {
                is Resource.Success -> _specificRestaurantState.update { state ->
                    delay(1000L)
                    state.copy(
                        transformedRestaurant = it.result,
                        isLoading = false
                    )
                }
                is Resource.Failure -> {
                    _specificRestaurantState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (it.error !is ResourceError.Default) return@onEach
                    val defaultError = it.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _specificRestaurantState.update { state ->
                    state.copy(isLoading = it.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            lateinit var oldState: SpecificRestaurantState
            val restaurant = _specificRestaurantState.value.transformedRestaurant
            _specificRestaurantState.update { state ->
                oldState = state
                state.copy(
                    transformedRestaurant = state.transformedRestaurant.apply {
                        mutableListOf(this)[0] = restaurant.copy(
                            isFavouriteByCurrentUser = !restaurant.isFavouriteByCurrentUser
                        )
                    }
                )
            }
            when (val resource = toggleFavouritesUseCase.togglingFavouriteInDetails(restaurant)) {
                is Resource.Failure -> {
                    _specificRestaurantState.value = oldState
                    if (resource.error !is ResourceError.Default) return@launch
                    val defaultError = resource.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                else -> Unit
            }
        }
    }
}