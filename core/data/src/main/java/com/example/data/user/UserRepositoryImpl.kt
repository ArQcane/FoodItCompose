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
        profile_pic: File?,
        deleteImage: Boolean?,
        fcmToken: String?
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
            fcmToken = fcmToken,
            deleteImage = deleteImage
        )
    )

    override suspend fun deleteAccount(userId: String): Resource<String> =
        remoteUserDao.deleteAccount(userId = userId)

    override suspend fun login(email: String, password: String): Resource<String> = remoteUserDao.login(
        loginDto = LoginDto(
            email = email,
            password = password
        )
    )

    override suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        email: String,
        phoneNumber: Int,
        gender: String,
        address: String,
        profile_pic: String?,
        fcmToken: String
    ): Resource<String> = remoteUserDao.register(
        registerDto = RegisterDto(
            firstName = firstName,
            lastName = lastName,
            username = username,
            password = password,
            email = email,
            phoneNumber = phoneNumber,
            gender = gender,
            address = address,
            fcmToken = fcmToken,
            profile_pic = profile_pic,
        )
    )
}