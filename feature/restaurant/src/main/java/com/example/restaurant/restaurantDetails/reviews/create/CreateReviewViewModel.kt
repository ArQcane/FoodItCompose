package com.example.restaurant.restaurantDetails.reviews.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.restaurant.usecases.GetSpecificRestaurantUseCase
import com.example.domain.review.usecases.CreateReviewUseCase
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getSpecificRestaurantUseCase: GetSpecificRestaurantUseCase,
    private val getCurrentLoggedInUser: GetCurrentLoggedInUser,
    private val createReviewUseCase: CreateReviewUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _createReviewState = MutableStateFlow(CreateReviewState())
    val createReviewState = _createReviewState.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    init {
        savedStateHandle.get<String>("restaurantId")?.let {
            getSpecificRestaurant(it)
        }
        getCurrentUser()
    }

    private fun getCurrentUser() {
        getCurrentLoggedInUser().onEach {
            when (it) {
                is Resource.Success -> _createReviewState.update { state ->
                    state.copy(currentUserId = it.result.user_id.toString())
                }
                is Resource.Failure -> _errorChannel.send(
                    (it.error as ResourceError.Default).error
                )
                else -> Unit
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun getSpecificRestaurant(restaurantId: String) {
        getSpecificRestaurantUseCase(
            restaurantId = restaurantId,
        ).onEach {
            when (it) {
                is Resource.Success -> _createReviewState.update { state ->
                    delay(500L)
                    state.copy(
                        transformedRestaurant = it.result,
                        isLoading = false,
                    )
                }
                is Resource.Failure -> {
                    _createReviewState.update { state ->
                        state.copy(isLoading = false)
                    }
                    if (it.error !is ResourceError.Default) return@onEach
                    val defaultError = it.error as ResourceError.Default
                    _errorChannel.send(defaultError.error)
                }
                is Resource.Loading -> _createReviewState.update { state ->
                    state.copy(isLoading = it.isLoading)
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun createReview() {
        val restaurant = _createReviewState.value.transformedRestaurant ?: return
        createReviewUseCase(
            restaurantId = restaurant.id.toString(),
            review = _createReviewState.value.review,
            rating = _createReviewState.value.rating
        ).onEach {
            when (it) {
                is Resource.Loading -> _createReviewState.update { state ->
                    state.copy(isSubmitting = it.isLoading)
                }
                is Resource.Success -> _createReviewState.update { state ->
                    val newReviewList = restaurant.reviews.toMutableList().apply {
                        add(0, it.result)
                    }
                    state.copy(
                        isSubmitting = false,
                        rating = 0,
                        review = "",
                        isUpdated = true,
                        transformedRestaurant = restaurant.copy(
                            reviews = newReviewList,
                            averageRating = newReviewList.sumOf { it.rating } / newReviewList.size.toDouble(),
                            ratingCount = newReviewList.size,
                        ),
                    )
                }
                is Resource.Failure -> {
                    when (it.error) {
                        is ResourceError.Default -> {
                            _createReviewState.update { state ->
                                state.copy(isSubmitting = false)
                            }
                            _errorChannel.send((it.error as ResourceError.Default).error)
                        }
                        is ResourceError.Field -> _createReviewState.update { state ->
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
        _createReviewState.update { state ->
            state.copy(isAnimationDone = isDone)
        }
    }

    private fun getRating(newRating: Int, oldRating: Int): Int {
        if (newRating == oldRating && newRating != 1) return newRating - 1
        return newRating
    }

    fun onEvent(event: ReviewEvent) {
        when (event) {
            is ReviewEvent.AnimationOverEvent -> {
                setAnimationIsDone(isDone = event.isAnimationDone)
            }
            is ReviewEvent.OnReviewChangedEvent -> _createReviewState.update { state ->
                state.copy(
                    review = event.review,
                    reviewError = null
                )
            }
            is ReviewEvent.OnRatingChangedEvent -> _createReviewState.update { state ->
                state.copy(
                    rating = getRating(
                        newRating = event.rating,
                        oldRating = state.rating
                    ),
                    ratingError = null
                )
            }
            is ReviewEvent.OnSubmit -> {
                createReview()
            }
        }
    }
}