package com.example.data.user.remote.dto

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
    val profile_pic: String? = null,
    val deleteImage: Boolean? = null,
)