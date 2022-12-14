package com.example.authentication.register

import android.graphics.Bitmap

data class RegisterState(
    val first_name: String = "",
    val last_name: String = "",
    val username: String = "",
    val user_pass: String = "",
    val confirmUserPass: String = "",
    val gender: String = "",
    val mobile_number: Long = 65,
    val email: String = "",
    val address: String = "",
    val profile_pic: Bitmap? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val usernameError: String? = null,
    val userPassError: String? = null,
    val confirmUserPassError: String? = null,
    val genderError: String? = null,
    val mobileNumberError: String? = null,
    val emailError: String? = null,
    val addressError: String? = null,
    val profilePicError: String? = null,
    val isLoading: Boolean = false,
    val isCreated: Boolean = false,
)