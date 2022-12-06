package com.example.fooditcompose.data.user.remote

import com.example.fooditcompose.data.user.remote.dto.LoginDto
import com.example.fooditcompose.domain.user.User
import com.example.fooditcompose.data.user.remote.dto.RegisterDto
import com.example.fooditcompose.data.user.remote.dto.TokenDto
import com.example.fooditcompose.data.user.remote.dto.UpdateAccountDto
import com.example.fooditcompose.data.common.dtos.DefaultMessageDto
import com.example.fooditcompose.data.utils.tryWithIoExceptionHandling
import com.example.fooditcompose.domain.utils.Resource
import com.example.fooditcompose.domain.utils.ResourceError
import com.example.fooditcompose.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.fooditcompose.utils.decodeFromJson
import com.example.fooditcompose.utils.toJson
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException
import javax.inject.Inject

class RemoteUserDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson,
) : RemoteUserDao(okHttpClient, gson) {
    companion object {
        const val FORGOT_PASSWORD_ENDPOINT = "/resetPassword"
    }

    override suspend fun getAllUsers(): Resource<List<User>> =
        tryWithIoExceptionHandling {
            val response = get()
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun getUserById(id: String): Resource<User> =
        tryWithIoExceptionHandling {
            val response = get(endpoint = "/id/$id")
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default("No user with id $id found")
                )
            return@tryWithIoExceptionHandling Resource.Success(
                gson.decodeFromJson(json)
            )
        }

    override suspend fun validateToken(token: String): Resource<User> =
        tryWithIoExceptionHandling {
            val response = post(
                endpoint = "http://127.0.0.1:8080/members",
                body = mapOf("token" to token)
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }


    override suspend fun forgotPassword(email: String): Resource<String> =
        tryWithIoExceptionHandling {
            val response = post(
                endpoint = FORGOT_PASSWORD_ENDPOINT,
                body = mapOf("email" to email),
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson<DefaultMessageDto>(json).message
                )
                400 -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Field>(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }


    override suspend fun updateAccount(
        updateAccountDto: UpdateAccountDto
    ): Resource<String> = tryWithIoExceptionHandling {
        val response = put(
            endpoint = "/updateuser",
            body = updateAccountDto.copy(),
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoExceptionHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoExceptionHandling when (response.code) {
            200 -> Resource.Success(
                gson.decodeFromJson<TokenDto>(json).token
            )
            else -> Resource.Failure(
                gson.decodeFromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun deleteAccount(
        userId: String
    ): Resource<String>  = tryWithIoExceptionHandling {
        val response = delete<Response>(
            endpoint = "/deleteuser/$userId"
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoExceptionHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoExceptionHandling when (response.code) {
            200 -> Resource.Success(
                gson.decodeFromJson<DefaultMessageDto>(json).message
            )
            400 -> Resource.Failure(
                gson.decodeFromJson<ResourceError.Field>(json)
            )
            else -> Resource.Failure(
                gson.decodeFromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun login(loginDto: LoginDto): Resource<String>  =
        tryWithIoExceptionHandling {
            val response = post(
                endpoint = "/login",
                body = loginDto
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson<TokenDto>(json).token
                )
                400 -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Field>(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun register(registerDto: RegisterDto): Resource<String> =
        tryWithIoExceptionHandling {
            val response = post(
                endpoint = "/register",
                body = registerDto.copy(),
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson<TokenDto>(json).token
                )
                400 -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Field>(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }

}
