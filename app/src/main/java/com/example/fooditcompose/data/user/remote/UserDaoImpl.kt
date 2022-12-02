package com.example.fooditcompose.data.user.remote

import com.example.fooditcompose.data.user.User
import com.example.fooditcompose.data.user.remote.dto.RegisterDto
import com.example.fooditcompose.data.user.remote.dto.UpdateAccountDto
import com.example.fooditcompose.domain.common.dtos.DefaultErrorDto
import com.example.fooditcompose.utils.Constants.Companion.BASE_URL
import com.example.fooditcompose.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.fooditcompose.utils.Resource
import com.example.fooditcompose.utils.await
import com.example.fooditcompose.utils.decodeFromJson
import com.example.fooditcompose.utils.toJson
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import javax.inject.Inject

class UserDaoImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
): UserDao{
    companion object{
        const val BASE_URL_USER = "$BASE_URL/users"
    }

    override suspend fun getAllUsers(): Resource<List<User>> {
        val request = Request.Builder()
            .url(BASE_URL_USER)
            .build()
        try {
            val response = okHttpClient.newCall(request).await()
            val json = response.body?.toJson()
            json ?: return Resource.Failure(UNABLE_GET_BODY_ERROR_MESSAGE)
            if (response.code == 200)
                return Resource.Success(
                    gson.decodeFromJson(json)
                )
            val errorDto = gson.decodeFromJson<DefaultErrorDto>(json)
            return Resource.Failure(errorDto.error)
        } catch (e: IOException) {
            return Resource.Failure(e.message.toString())
        }
    }

    override suspend fun getUserById(id: String): Resource<User> {
        val request = Request.Builder()
            .url("$BASE_URL_USER/id/$id")
            .build()
        try {
            val response = okHttpClient.newCall(request).await()
            val json = response.body?.toJson() ?: return Resource.Failure(
                "No user with id $id found"
            )
            return Resource.Success(
                gson.decodeFromJson(json)
            )
        } catch (e: IOException) {
            return Resource.Failure(e.message.toString())
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
        val json = "{\"username\":\"$username\",\"user_pass\":\"$password\"}"
        val body = json.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$BASE_URL_USER/login")
            .post(body)
            .build()
        try {
            val response = okHttpClient.newCall(request).await()
            val json = response.body?.toJson() ?: return Resource.Failure(
                "Invalid username or password entered"
            )
            return Resource.Success(
                gson.decodeFromJson(json)
            )
        } catch (e: IOException) {
            return Resource.Failure(e.message.toString())
        }
    }

    override suspend fun register(signUpDto: RegisterDto): Resource<String> {
        TODO("Not yet implemented")
    }
}