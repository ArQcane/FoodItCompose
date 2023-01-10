package com.example.data.user.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateAccountDto(
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("mobile_number")
    val phoneNumber: Long? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("profile_pic")
    val profile_pic: String? = null,
)