package com.example.fooditcompose.data.user.remote

import OkHttpDao
import com.example.fooditcompose.data.user.remote.dto.LoginDto
import com.example.fooditcompose.domain.user.User
import com.example.fooditcompose.data.user.remote.dto.RegisterDto
import com.example.fooditcompose.data.user.remote.dto.UpdateAccountDto

import com.example.fooditcompose.domain.utils.Resource

import okhttp3.OkHttpClient

interface RemoteUserDao {
    suspend fun getAllUsers(): Resource<List<User>>
    suspend fun getUserById(id: String): Resource<User>
    suspend fun validateToken(token: String): Resource<User>
    suspend fun forgotPassword(email: String): Resource<String>
    suspend fun updateAccount(updateAccountDto: UpdateAccountDto): Resource<String>
    suspend fun deleteAccount(userId: String): Resource<String>
    suspend fun login(loginDto: LoginDto): Resource<String>
    suspend fun register(registerDto: RegisterDto): Resource<String>
}