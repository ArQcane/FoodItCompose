package com.example.data.user.remote.dto

import java.io.File

data class RegisterDto(
    val first_name: String,
    val last_name: String,
    val username: String,
    val user_pass: String,
    val email: String,
    val mobile_number: Long,
    val gender: String,
    val address: String,
    val profile_pic: String? = null,
)