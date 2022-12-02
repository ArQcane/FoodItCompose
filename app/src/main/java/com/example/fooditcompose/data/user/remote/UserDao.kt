package com.example.fooditcompose.data.user.remote

import com.example.fooditcompose.data.user.User
import com.example.fooditcompose.data.user.remote.dto.RegisterDto
import com.example.fooditcompose.data.user.remote.dto.UpdateAccountDto
import com.example.fooditcompose.utils.Resource

interface UserDao {
    suspend fun getAllUsers(): Resource<List<User>>
    suspend fun getUserById(id: String): Resource<User>
    suspend fun forgotPassword(email: String): Resource<String>
    suspend fun updateAccount(updateAccountDto: UpdateAccountDto): Resource<String>
    suspend fun deleteAccount(password: String): Resource<String>
    suspend fun login(username: String, password: String): Resource<String>
    suspend fun register(signUpDto: RegisterDto): Resource<String>
}