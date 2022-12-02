package com.example.fooditcompose.data.user.remote

import com.example.fooditcompose.domain.user.User
import com.example.fooditcompose.data.user.remote.dto.RegisterDto
import com.example.fooditcompose.data.user.remote.dto.UpdateAccountDto
import com.example.fooditcompose.domain.common.OkHttpDao
import com.example.fooditcompose.domain.utils.Resource
import com.google.gson.Gson
import okhttp3.OkHttpClient

abstract class UserDao(
    okHttpClient: OkHttpClient,
    gson: Gson
) : OkHttpDao(okHttpClient, gson, "/users") {
    abstract suspend fun getAllUsers(): Resource<List<User>>
    abstract suspend fun getUserById(id: String): Resource<User>
    abstract suspend fun forgotPassword(email: String): Resource<String>
    abstract suspend fun updateAccount(updateAccountDto: UpdateAccountDto): Resource<String>
    abstract suspend fun deleteAccount(password: String): Resource<String>
    abstract suspend fun login(username: String, password: String): Resource<String>
    abstract suspend fun register(signUpDto: RegisterDto): Resource<String>
}