package com.example.fooditcompose.data.user.remote.dto

import java.io.File

data class UpdateAccountDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val password: String? = null,
    val email: String? = null,
    val phoneNumber: Int? = null,
    val gender: String? = null,
    val address: String? = null,
    val image: File? = null,
    val deleteImage: Boolean? = null,
    val fcmToken: String? = null,
)