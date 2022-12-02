package com.example.fooditcompose.data.user.remote

import com.example.fooditcompose.domain.user.User
import com.example.fooditcompose.data.user.remote.dto.RegisterDto
import com.example.fooditcompose.data.user.remote.dto.TokenDto
import com.example.fooditcompose.data.user.remote.dto.UpdateAccountDto
import com.example.fooditcompose.domain.utils.Resource
import com.example.fooditcompose.domain.utils.ResourceError
import com.example.fooditcompose.utils.Constants.Companion.BASE_URL
import com.example.fooditcompose.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.fooditcompose.utils.await
import com.example.fooditcompose.utils.decodeFromJson
import com.example.fooditcompose.utils.toJson
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import javax.inject.Inject

class UserDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson,
) : UserDao(okHttpClient, gson) {

    override suspend fun getAllUsers(): Resource<List<User>> {
        try {
            val response = get()
            val json = response.body?.toJson()
            json ?: return Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
            if (response.code == 200) return Resource.Success(
                gson.decodeFromJson(json)
            )
            return Resource.Failure(
                gson.decodeFromJson<ResourceError.Default>(json)
            )
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }

    override suspend fun getUserById(id: String): Resource<User> {
        try {
            val response = get("/id/$id")
            val json = response.body?.toJson() ?: return Resource.Failure(
                ResourceError.Default("No user with id $id found")
            )
            return Resource.Success(
                gson.decodeFromJson(json)
            )
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }


    override suspend fun forgotPassword(email: String): Resource<String> {
        TODO("Not yet implemented")
    }


    override suspend fun updateAccount(updateAccountDto: UpdateAccountDto): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(password: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun login(username: String, password: String): Resource<String> {
        try {
            val response = post(
                "/login",
                hashMapOf("username" to username, "user_pass" to password)
            )
            val json = response.body?.toJson() ?: return Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
            if (response.code == 200) return Resource.Success(
                gson.decodeFromJson<TokenDto>(json).token
            )
            if (response.code == 400) return Resource.Failure(
                gson.decodeFromJson<ResourceError.Field>(json)
            )
            return Resource.Failure(
                gson.decodeFromJson<ResourceError.Default>(json)
            )
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }

    override suspend fun register(registerDto: RegisterDto): Resource<String> {
        try {
            val response = post(
                "/create-account",
                hashMapOf(
                    "first_name" to registerDto.firstName,
                    "last_name" to registerDto.lastName,
                    "username" to registerDto.username,
                    "user_pass" to registerDto.password,
                    "gender" to registerDto.gender,
                    "mobile_number" to registerDto.phoneNumber,
                    "email" to registerDto.email,
                    "address" to registerDto.address,
                    "profile_pic" to registerDto.profile_pic!!,
                    "fcmToken" to registerDto.fcmToken,
                ),
            )
            val json = response.body?.toJson() ?: return Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
            if (response.code == 200) return Resource.Success(
                gson.decodeFromJson<TokenDto>(json).token
            )
            if (response.code == 400) return Resource.Failure(
                gson.decodeFromJson<ResourceError.Field>(json)
            )
            return Resource.Failure(
                gson.decodeFromJson<ResourceError.Default>(json)
            )
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }
}
