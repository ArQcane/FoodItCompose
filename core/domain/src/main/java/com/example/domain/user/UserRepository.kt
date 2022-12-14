package com.example.domain.user

import com.example.domain.utils.Resource
import java.io.File

interface UserRepository {
    fun getToken(): Resource<String>
    fun saveToken(token: String)

    suspend fun getAllUsers(): Resource<List<User>>
    suspend fun getUserById(id: String): Resource<User>
    suspend fun validateToken(token: String): Resource<User>
    suspend fun forgotPassword(email: String): Resource<String>
    suspend fun updateAccount(
        firstName: String? = null,
        lastName: String? = null,
        username: String? = null,
        password: String? = null,
        email: String? = null,
        phoneNumber: Int? = null,
        gender: String? = null,
        address: String? = null,
        profile_pic: File? = null,
        deleteImage: Boolean? = null,
        fcmToken: String? = null,
    ): Resource<String>

    suspend fun deleteAccount(userId: String): Resource<String>
    suspend fun login(
        email: String,
        password: String
    ): Resource<String>

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        email: String,
        phoneNumber: Int,
        gender: String,
        address: String,
        profile_pic: String? = null,
        fcmToken: String,
    ): Resource<String>
}