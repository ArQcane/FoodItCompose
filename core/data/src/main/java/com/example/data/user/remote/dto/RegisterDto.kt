package com.example.data.user.remote.dto

import java.io.File

data class RegisterDto(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: Int,
    val gender: String,
    val address: String,
    val profile_pic: String? = null,
    val fcmToken: String,
)