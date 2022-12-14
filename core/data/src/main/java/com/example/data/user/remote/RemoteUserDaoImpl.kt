package com.example.data.user.remote


import com.example.data.common.DefaultMessageDto
import com.example.data.user.remote.RemoteUserDao
import com.example.data.user.remote.dto.LoginDto
import com.example.data.user.remote.dto.RegisterDto
import com.example.data.user.remote.dto.TokenDto
import com.example.data.user.remote.dto.UpdateAccountDto
import com.example.data.utils.tryWithIoHandling
import com.example.domain.user.User
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.network.Authorization
import com.example.network.OkHttpDao
import com.example.network.converter.JsonConverter
import com.example.network.delegations.AuthorizationImpl
import com.example.network.delegations.OkHttpDaoImpl
import com.example.network.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.network.utils.toJson
import okhttp3.OkHttpClient
import javax.inject.Inject

class RemoteUserDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    converter: JsonConverter,
) : RemoteUserDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        converter = converter,
        okHttpClient = okHttpClient,
        path = "/users"
    ) {

    companion object {
        const val FORGOT_PASSWORD_ENDPOINT = "/resetPassword"
        const val LOGIN_ENDPOINT = "/login"
        const val SIGN_UP_ENDPOINT = "/register"
        const val VALIDATE_TOKEN_ENDPOINT = "/members"
    }

    override suspend fun getAllUsers(): Resource<List<User>> =
        tryWithIoHandling {
            val response = get()
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun getUserById(id: String): Resource<User> =
        tryWithIoHandling {
            val response = get(endpoint = "/id/$id")
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default("No user with id $id found")
                )
            return@tryWithIoHandling Resource.Success(
                converter.fromJson(json)
            )
        }

    override suspend fun validateToken(token: String): Resource<User> =
        tryWithIoHandling {
            val response = get(
                endpoint = VALIDATE_TOKEN_ENDPOINT,
                headers = createAuthorizationHeader(token)
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun forgotPassword(email: String): Resource<String> =
        tryWithIoHandling {
            val response = post(
                endpoint = FORGOT_PASSWORD_ENDPOINT,
                body = mapOf("email" to email),
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson<DefaultMessageDto>(json).message
                )
                400 -> Resource.Failure(
                    converter.fromJson<ResourceError.Field>(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun updateAccount(
        updateAccountDto: UpdateAccountDto
    ): Resource<String> = tryWithIoHandling {
        val response = put(
            endpoint = "/updateuser",
            body = updateAccountDto.copy(),
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoHandling when (response.code) {
            200 -> Resource.Success(
                converter.fromJson<TokenDto>(json).token
            )
            else -> Resource.Failure(
                converter.fromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun deleteAccount(
        userId: String
    ): Resource<String> = tryWithIoHandling {
        val response = delete<Unit>(
            endpoint = "/deleteuser/$userId"
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoHandling when (response.code) {
            200 -> Resource.Success(
                converter.fromJson<DefaultMessageDto>(json).message
            )
            400 -> Resource.Failure(
                converter.fromJson<ResourceError.Field>(json)
            )
            else -> Resource.Failure(
                converter.fromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun login(loginDto: LoginDto): Resource<String> =
        tryWithIoHandling {
            val response = post(
                endpoint = LOGIN_ENDPOINT,
                body = loginDto
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson<TokenDto>(json).token
                )
                400 -> Resource.Failure(
                    converter.fromJson<ResourceError.Field>(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun register(registerDto: RegisterDto): Resource<String> =
        tryWithIoHandling {
            val response = post(
                endpoint = SIGN_UP_ENDPOINT,
                body = registerDto.copy(),
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson<TokenDto>(json).token
                )
                400 -> Resource.Failure(
                    converter.fromJson<ResourceError.Field>(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

}
