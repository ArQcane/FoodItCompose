package com.example.fooditcompose.ui.screens.profile

import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.domain.user.User

data class ProfileState(
    val user: User = User(
        user_id = 0,
        first_name = "",
        last_name= "",
        username= "",
        gender= "",
        mobile_number = 0,
        email = "",
        address = "",
        profile_pic = "",
),
val isLoading: Boolean = true,
val totalReviews: Int =  0,
)