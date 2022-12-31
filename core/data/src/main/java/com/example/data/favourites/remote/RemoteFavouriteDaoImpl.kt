package com.example.data.favourites.remote

import com.example.data.common.DefaultMessageDto
import com.example.data.utils.Constants.NO_RESPONSE
import com.example.data.utils.tryWithIoHandling
import com.example.domain.restaurant.Restaurant
import com.example.domain.user.User
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.network.Authorization
import com.example.network.OkHttpDao
import com.example.network.delegations.AuthorizationImpl
import com.example.network.delegations.OkHttpDaoImpl
import com.example.network.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.network.utils.toJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import javax.inject.Inject

class RemoteFavoriteDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson
) : RemoteFavoriteDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        okHttpClient = okHttpClient,
        gson = gson,
        path = "/favourites"
    ) {

    override suspend fun getFavoriteRestaurantsOfUser(userId: String): Resource<List<Restaurant>> =
        tryWithIoHandling {
            val (json, code) = get(endpoint = "/user/$userId")
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<List<Restaurant>>() {}.type
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

    override suspend fun getUsersWhoFavoriteRestaurant(restaurantId: String): Resource<List<com.example.domain.user.User>> =
        tryWithIoHandling {
            val (json, code) = get(endpoint = "/restaurant/$restaurantId")
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

    override suspend fun addFavorite(
        userId: String,
        restaurantId: String
    ): Resource<String> = tryWithIoHandling {
        val (json,code) = post(
            endpoint = "/createFav",
            body = mapOf("userID" to userId, "restaurantID" to restaurantId),
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
            else -> Resource.Failure(
                gson.fromJson<ResourceError.Default>(
                    json,
                    object : TypeToken<ResourceError.Default>() {}.type
                )
            )
        }
    }

    override suspend fun removeFavorite(
        userId: String,
        restaurantId: String
    ): Resource<String> = tryWithIoHandling {
        val (json,code) = delete(
            endpoint = "/deleteFav",
            body = mapOf("userID" to userId, "restaurantID" to restaurantId),
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
            else -> Resource.Failure(
                gson.fromJson<ResourceError.Default>(
                    json,
                    object : TypeToken<ResourceError.Default>() {}.type
                )
            )
        }
    }
}