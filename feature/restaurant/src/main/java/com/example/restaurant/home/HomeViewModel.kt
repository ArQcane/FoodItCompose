package com.example.restaurant.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.favourites.usecases.ToggleFavouritesUseCase
import com.example.domain.restaurant.Restaurant
import com.example.domain.restaurant.TransformedRestaurant
import com.example.domain.restaurant.usecases.GetAllRestaurantsUseCase
import com.example.domain.restaurant.usecases.GetExpensiveRestaurantsUseCase
import com.example.domain.user.UserRepository
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
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getAllRestaurantsUseCase: GetAllRestaurantsUseCase,
    private val getExpensiveRestaurantsUseCase: GetExpensiveRestaurantsUseCase,
    private val toggleFavouritesUseCase: ToggleFavouritesUseCase,
) : ViewModel() {
    private val _restaurantState = MutableStateFlow(HomeState())
    val restaurantState = _restaurantState.asStateFlow()

    private val _expensiveRestaurantState = MutableStateFlow(HomeState())
    val expensiveRestaurantState = _expensiveRestaurantState.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()


    init {
        getRestaurants()
        getExpensiveRestaurants()
    }

    private fun getRestaurants() {
        getAllRestaurantsUseCase().onEach {
            when (it) {
                is Resource.Success -> _restaurantState.update { state ->
                    delay(1000L)
                    state.copy(
                        restaurantList = it.result,
                        isLoading = false
                    )
                }
                is Resource.Failure -> {
                    _restaurantState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (it.error !is ResourceError.Default) return@onEach
                    val defaultError = it.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _restaurantState.update { state ->
                    state.copy(isLoading = it.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }
    private fun getExpensiveRestaurants(){
        getExpensiveRestaurantsUseCase().onEach {
            when (it) {
                is Resource.Success -> _expensiveRestaurantState.update { state ->
                    delay(1000L)
                    state.copy(
                        restaurantList = it.result,
                        isLoading = false
                    )
                }
                is Resource.Failure -> {
                    _expensiveRestaurantState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (it.error !is ResourceError.Default) return@onEach
                    val defaultError = it.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _expensiveRestaurantState.update { state ->
                    state.copy(isLoading = it.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            val index = _restaurantState.value.restaurantList.map { it.id }.indexOf(restaurantId.toInt())
            val restaurant = _restaurantState.value.restaurantList[index]
            lateinit var oldState: HomeState
            _restaurantState.update { state ->
                oldState = state
                state.copy(
                    restaurantList = state.restaurantList.toMutableList().apply {
                        set(
                            index,
                            restaurant.copy(
                                isFavouriteByCurrentUser = !restaurant.isFavouriteByCurrentUser
                            )
                        )
                    }
                )
            }
            when (val resource = toggleFavouritesUseCase(restaurant)) {
                is Resource.Failure -> {
                    _restaurantState.value = oldState
                    if (resource.error !is ResourceError.Default) return@launch
                    val defaultError = resource.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                else -> Unit
            }
        }
    }
}