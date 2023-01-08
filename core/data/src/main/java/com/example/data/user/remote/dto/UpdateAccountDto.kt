package com.example.data.user.remote.dto

import java.io.File

data class UpdateAccountDto(
    val userId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: Long? = null,
    val address: String? = null,
    val profile_pic: String? = null,
    val deleteImage: Boolean? = null,
)