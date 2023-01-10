package com.example.restaurant.restaurantDetails.reviews

sealed class CreateReviewEvent {
    class AnimationOverEvent(val isAnimationDone: Boolean) : CreateReviewEvent()
    class OnReviewChangedEvent(val review: String) : CreateReviewEvent()
    class OnRatingChangedEvent(val rating: Int) : CreateReviewEvent()
    object OnSubmit : CreateReviewEvent()
}