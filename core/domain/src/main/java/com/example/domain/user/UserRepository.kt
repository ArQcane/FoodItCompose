package com.example.domain.user

import com.example.domain.utils.Resource
import java.io.File

interface UserRepository {
    fun getToken(): Resource<String>
    fun saveToken(token: String)
    fun deleteToken()

    suspend fun getAllUsers(): Resource<List<User>>
    suspend fun getUserById(id: String): Resource<ReviewUser>
    suspend fun validateToken(token: String): Resource<User>
    suspend fun forgotPassword(email: String): Resource<String>
    suspend fun updateAccount(
        userId: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: Long? = null,
        address: String? = null,
        profile_pic: String? = null,
        deleteImage: Boolean? = null,
    ): Resource<String>

    suspend fun deleteAccount(userId: String): Resource<String>
    suspend fun login(
        username: String,
        user_pass: String
    ): Resource<String>

    suspend fun register(
        first_name: String,
        last_name: String,
        username: String,
        user_pass: String,
        email: String,
        mobile_number: Long,
        gender: String,
        address: String,
        profile_pic: String? = null
    ): Resource<String>


}