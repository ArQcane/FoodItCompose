package com.example.restaurant.restaurantDetails.reviews.create

sealed class ReviewEvent {
    class AnimationOverEvent(val isAnimationDone: Boolean) : ReviewEvent()
    class OnReviewChangedEvent(val review: String) : ReviewEvent()
    class OnRatingChangedEvent(val rating: Int) : ReviewEvent()
    object OnSubmit : ReviewEvent()
    class DeleteReview(val index: Int) : ReviewEvent()
    class OpenEditReviewDialog(val index: Int) : ReviewEvent()
    object OnCloseEditReviewDialog : ReviewEvent()
    class OnEditReview(val review: String) : ReviewEvent()
    class OnEditRating(val rating: Int) : ReviewEvent()
    object OnCompleteEdit : ReviewEvent()
}