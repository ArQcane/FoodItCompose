package com.example.user.profile.editProfile

import com.example.domain.user.User

data class EditProfileState(
    val user_id: Int = 0,
    val first_name: String = "",
    val last_name: String = "",
    val username: String = "",
    val gender: String = "",
    val mobile_number: Long = 0,
    val email: String = "",
    val address: String = "",
    val profile_pic: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val mobileNumberError: String? = null,
    val addressError: String? = null,
    val profilePicError: String? = null,
    val isLoading: Boolean = true,
    val isUpdated: Boolean = false,
)