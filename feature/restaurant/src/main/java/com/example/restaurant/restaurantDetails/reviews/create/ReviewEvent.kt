package com.example.restaurant.restaurantDetails.reviews.create

sealed class ReviewEvent {
    class AnimationOverEvent(val isAnimationDone: Boolean) : ReviewEvent()
    class OnReviewChangedEvent(val review: String) : ReviewEvent()
    class OnRatingChangedEvent(val rating: Int) : ReviewEvent()
    object OnSubmit : ReviewEvent()
    class DeleteComment(val index: Int) : ReviewEvent()
    class OpenEditCommentDialog(val index: Int) : ReviewEvent()
    object OnCloseEditCommentDialog : ReviewEvent()
    class OnEditReview(val review: String) : ReviewEvent()
    class OnEditRating(val rating: Int) : ReviewEvent()
    object OnCompleteEdit : ReviewEvent()
}