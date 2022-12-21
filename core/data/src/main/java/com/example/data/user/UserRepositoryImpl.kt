package com.example.data.user

import com.example.data.user.local.SharedPreferenceDao
import com.example.data.user.remote.RemoteUserDao
import com.example.data.user.remote.dto.LoginDto
import com.example.data.user.remote.dto.RegisterDto
import com.example.data.user.remote.dto.UpdateAccountDto
import com.example.domain.user.User
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteUserDao: RemoteUserDao,
    private val sharedPreferenceDao: SharedPreferenceDao
) : UserRepository {
    override fun getToken(): Resource<String> =
        sharedPreferenceDao.getToken()

    override fun saveToken(token: String) =
        sharedPreferenceDao.saveToken(token)

    override fun deleteToken() =
        sharedPreferenceDao.deleteToken()

    override suspend fun getAllUsers(): Resource<List<User>> =
        remoteUserDao.getAllUsers()

    override suspend fun getUserById(id: String): Resource<User> =
        remoteUserDao.getUserById(id = id)

    override suspend fun validateToken(token: String): Resource<User> =
        remoteUserDao.validateToken(token = token)

    override suspend fun forgotPassword(email: String): Resource<String> =
        remoteUserDao.forgotPassword(email = email)

    override suspend fun updateAccount(
        firstName: String?,
        lastName: String?,
        username: String?,
        password: String?,
        email: String?,
        phoneNumber: Int?,
        gender: String?,
        address: String?,
        profile_pic: String?,
        deleteImage: Boolean?,
    ): Resource<String> = remoteUserDao.updateAccount(
        updateAccountDto = UpdateAccountDto(
            firstName = firstName,
            lastName = lastName,
            username = username,
            password = password,
            email = email,
            phoneNumber = phoneNumber,
            gender = gender,
            address = address,
            profile_pic = profile_pic,
            deleteImage = deleteImage
        )
    )

    override suspend fun deleteAccount(userId: String): Resource<String> =
        remoteUserDao.deleteAccount(userId = userId)

    override suspend fun login(username: String, user_pass: String): Resource<String> = remoteUserDao.login(
        loginDto = LoginDto(
            username = username,
            user_pass = user_pass
        )
    )

    override suspend fun register(
        first_name: String,
        last_name: String,
        username: String,
        user_pass: String,
        email: String,
        mobile_number: Int,
        gender: String,
        address: String,
        profile_pic: String?,
    ): Resource<String> = remoteUserDao.register(
        registerDto = RegisterDto(
            first_name = first_name,
            last_name = last_name,
            username = username,
            user_pass = user_pass,
            email = email,
            mobile_number = mobile_number,
            gender = gender,
            address = address,
            profile_pic = profile_pic,
        )
    )
}