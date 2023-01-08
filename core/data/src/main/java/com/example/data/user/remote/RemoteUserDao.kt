package com.example.data.user.remote


import com.example.data.user.remote.dto.LoginDto
import com.example.data.user.remote.dto.RegisterDto
import com.example.data.user.remote.dto.UpdateAccountDto
import com.example.domain.user.ReviewUser
import com.example.domain.user.User
import com.example.domain.utils.Resource


interface RemoteUserDao {
    suspend fun getAllUsers(): Resource<List<User>>
    suspend fun getUserById(id: String): Resource<ReviewUser>
    suspend fun validateToken(token: String): Resource<User>
    suspend fun forgotPassword(email: String): Resource<String>
    suspend fun updateAccount(userId: String, updateAccountDto: UpdateAccountDto): Resource<String>
    suspend fun deleteAccount(userId: String): Resource<String>
    suspend fun login(loginDto: LoginDto): Resource<String>
    suspend fun register(registerDto: RegisterDto): Resource<String>
}