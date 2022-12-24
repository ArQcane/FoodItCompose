package com.example.data.user.remote


import android.util.Log
import com.example.data.common.DefaultMessageDto
import com.example.data.user.remote.dto.*
import com.example.data.utils.Constants.NO_RESPONSE
import com.example.data.utils.tryWithIoHandling
import com.example.domain.user.User
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.network.Authorization
import com.example.network.OkHttpDao
import com.example.network.delegations.AuthorizationImpl
import com.example.network.delegations.OkHttpDaoImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import javax.inject.Inject

class RemoteUserDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson,
) : RemoteUserDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        gson = gson,
        okHttpClient = okHttpClient,
        path = "/users"
    ) {

    companion object {
        const val FORGOT_PASSWORD_ENDPOINT = "/forget-password"
        const val LOGIN_ENDPOINT = "/login"
        const val SIGN_UP_ENDPOINT = "/register"
        const val VALIDATE_TOKEN_ENDPOINT = "/members"
    }

    override suspend fun getAllUsers(): Resource<List<User>> =
        tryWithIoHandling {
            val (json, code) = get()
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<List<User>>() {}.type
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

    override suspend fun getUserById(id: String): Resource<User> =
        tryWithIoHandling {
            val (json, code) = get(endpoint = "/id/$id")
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default("No user with id $id found")
            )
            return@tryWithIoHandling Resource.Success(
                gson.fromJson(
                    json,
                    object : TypeToken<User>() {}.type
                )
            )
        }

    override suspend fun validateToken(token: String): Resource<User> =
        tryWithIoHandling {
            val (json, code) = get(
                endpoint = "$VALIDATE_TOKEN_ENDPOINT/$token",
            )
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<User>() {}.type
                    )
                )
                401 -> Resource.Failure(
                    ResourceError.Default(
                        gson.fromJson<TokenDto>(
                            json,
                            object : TypeToken<TokenDto>() {}.type
                        ).result
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

    override suspend fun forgotPassword(email: String): Resource<String> =
        tryWithIoHandling {
            val (json, code) = post(
                endpoint = FORGOT_PASSWORD_ENDPOINT,
                body = mapOf("email" to email),
            )
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson<DefaultMessageDto>(
                        json,
                        object : TypeToken<DefaultMessageDto>() {}.type
                    ).message
                )
                400 -> Resource.Failure(
                    gson.fromJson<ResourceError.Field>(
                        json,
                        object : TypeToken<ResourceError.Field>() {}.type
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

    override suspend fun updateAccount(
        updateAccountDto: UpdateAccountDto
    ): Resource<String> = tryWithIoHandling {
        val (json, code) = put(
            endpoint = "/updateuser",
            body = updateAccountDto.copy(),
        )
        json ?: return@tryWithIoHandling Resource.Failure(
            ResourceError.Default(NO_RESPONSE)
        )
        return@tryWithIoHandling when (code) {
            200 -> Resource.Success(
                gson.fromJson<TokenDto>(
                    json,
                    object : TypeToken<TokenDto>() {}.type
                ).result
            )
            else -> Resource.Failure(
                gson.fromJson<ResourceError.Default>(
                    json,
                    object : TypeToken<ResourceError.Default>() {}.type
                )
            )
        }
    }

    override suspend fun deleteAccount(
        userId: String
    ): Resource<String> = tryWithIoHandling {
        val (json, code) = delete<Unit>(
            endpoint = "/deleteuser/$userId"
        )
        json ?: return@tryWithIoHandling Resource.Failure(
            ResourceError.Default(NO_RESPONSE)
        )
        return@tryWithIoHandling when (code) {
            200 -> Resource.Success(
                gson.fromJson<DefaultMessageDto>(
                    json,
                    object : TypeToken<DefaultMessageDto>() {}.type
                ).message
            )
            400 -> Resource.Failure(
                gson.fromJson<ResourceError.Field>(
                    json,
                    object : TypeToken<ResourceError.Field>() {}.type
                )
            )
            else -> Resource.Failure(
                gson.fromJson<ResourceError.Default>(
                    json,
                    object : TypeToken<ResourceError.Default>() {}.type
                )
            )
        }
    }

    override suspend fun login(loginDto: LoginDto): Resource<String> =
        tryWithIoHandling {
            val (json, code) = post(
                endpoint = LOGIN_ENDPOINT,
                body = loginDto
            )
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson<TokenDto>(
                        json,
                        object : TypeToken<TokenDto>() {}.type
                    ).result
                )
                403 -> Resource.Failure(
                    ResourceError.Default(
                        gson.fromJson<TokenDto>(
                            json,
                            object : TypeToken<TokenDto>() {}.type
                        ).result
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

    override suspend fun register(registerDto: RegisterDto): Resource<String> =
        tryWithIoHandling {
            val (json, code) = post(
                endpoint = SIGN_UP_ENDPOINT,
                body = registerDto.copy(),
            )
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson<TokenDto>(
                        json,
                        object : TypeToken<TokenDto>() {}.type
                    ).result
                )
                400 -> Resource.Failure(
                    gson.fromJson<ResourceError.Field>(
                        json,
                        object : TypeToken<ResourceError.Field>() {}.type
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

}
