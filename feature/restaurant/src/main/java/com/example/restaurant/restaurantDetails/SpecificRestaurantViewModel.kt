package com.example.restaurant.restaurantDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.favourites.usecases.ToggleFavouritesUseCase
import com.example.domain.restaurant.usecases.GetSpecificRestaurantUseCase
import com.example.domain.review.usecases.CreateReviewUseCase
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.restaurant.restaurantDetails.reviews.CreateReviewEvent
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
    private val createReviewUseCase: CreateReviewUseCase,
    private val toggleFavouritesUseCase: ToggleFavouritesUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
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
                    delay(500L)
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
                    transformedRestaurant = restaurant.copy(
                        isFavouriteByCurrentUser = !restaurant.isFavouriteByCurrentUser
                    )
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

    private fun createReview() {
        val restaurant = _specificRestaurantState.value.transformedRestaurant ?: return
        createReviewUseCase(
            restaurantId = restaurant.id.toString(),
            review = _specificRestaurantState.value.review,
            rating = _specificRestaurantState.value.rating
        ).onEach {
            when (it) {
                is Resource.Loading -> _specificRestaurantState.update { state ->
                    state.copy(isSubmitting = it.isLoading)
                }
                is Resource.Success -> _specificRestaurantState.update { state ->
                    val newCommentList = restaurant.reviews.toMutableList().apply {
                        add(0, it.result)
                    }
                    state.copy(
                        isSubmitting = false,
                        rating = 0,
                        review = "",
                        isUpdated = true,
                        transformedRestaurant = restaurant.copy(
                            reviews = newCommentList,
                            averageRating = newCommentList.sumOf { it.rating } / newCommentList.size.toDouble(),
                            ratingCount = newCommentList.size,
                        ),
                    )
                }
                is Resource.Failure -> {
                    when (it.error) {
                        is ResourceError.Default -> {
                            _specificRestaurantState.update { state ->
                                state.copy(isSubmitting = false)
                            }
                            _errorChannel.send((it.error as ResourceError.Default).error)
                        }
                        is ResourceError.Field -> _specificRestaurantState.update { state ->
                            val fieldErrors = (it.error as ResourceError.Field).errors
                            state.copy(
                                isSubmitting = false,
                                reviewError = fieldErrors.find { it.field == "review" }?.error,
                                ratingError = fieldErrors.find { it.field == "rating" }?.error
                            )
                        }
                    }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun setAnimationIsDone(isDone: Boolean) {
        _specificRestaurantState.update { state ->
            state.copy(isAnimationDone = isDone)
        }
    }

    private fun getRating(newRating: Int, oldRating: Int): Int {
        if (newRating == oldRating && newRating != 1) return newRating - 1
        return newRating
    }

    fun onEvent(event: CreateReviewEvent) {
        when (event) {
            is CreateReviewEvent.AnimationOverEvent -> {
                setAnimationIsDone(isDone = event.isAnimationDone)
            }
            is CreateReviewEvent.OnReviewChangedEvent -> _specificRestaurantState.update { state ->
                state.copy(
                    review = event.review,
                    reviewError = null
                )
            }
            is CreateReviewEvent.OnRatingChangedEvent -> _specificRestaurantState.update { state ->
                state.copy(
                    rating = getRating(
                        newRating = event.rating,
                        oldRating = state.rating
                    ),
                    ratingError = null
                )
            }
            is CreateReviewEvent.OnSubmit -> {
                createReview()
            }
        }
    }
}