package com.example.domain.user

data class User(
    val user_id: Int,
    val first_name: String,
    val last_name: String,
    val username: String,
    val gender: String,
    val mobile_number: Long,
    val email: String,
    val address: String,
    val profile_pic: String?,
)



