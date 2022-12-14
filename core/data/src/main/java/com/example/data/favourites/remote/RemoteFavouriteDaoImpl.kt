package com.example.data.favourites.remote

import com.example.data.common.DefaultMessageDto
import com.example.data.utils.tryWithIoHandling
import com.example.domain.restaurant.Restaurant
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

class RemoteFavoriteDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    jsonConverter: JsonConverter
) : RemoteFavoriteDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        okHttpClient = okHttpClient,
        converter = jsonConverter,
        path = "/favourites"
    ) {

    override suspend fun getFavoriteRestaurantsOfUser(userId: String): Resource<List<Restaurant>> =
        tryWithIoHandling {
            val response = get(endpoint = "/user/$userId")
            val json = response.body?.toJson() ?: return@tryWithIoHandling Resource.Failure(
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

    override suspend fun getUsersWhoFavoriteRestaurant(restaurantId: String): Resource<List<User>> =
        tryWithIoHandling {
            val response = get(endpoint = "/restaurant/$restaurantId")
            val json = response.body?.toJson() ?: return@tryWithIoHandling Resource.Failure(
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

    override suspend fun addFavorite(
        userId: String,
        restaurantId: String
    ): Resource<String> = tryWithIoHandling {
        val response = post(
            endpoint = "/createFav",
            body = mapOf("userID" to userId, "restaurantID" to restaurantId),
        )
        val json = response.body?.toJson() ?: return@tryWithIoHandling Resource.Failure(
            ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
        )
        return@tryWithIoHandling when (response.code) {
            200 -> Resource.Success(
                converter.fromJson<DefaultMessageDto>(json).message
            )
            else -> Resource.Failure(
                converter.fromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun removeFavorite(
        userId: String,
        restaurantId: String
    ): Resource<String> = tryWithIoHandling {
        val response = delete(
            endpoint = "/deleteFav",
            body = mapOf("userID" to userId, "restaurantID" to restaurantId),
        )
        val json = response.body?.toJson() ?: return@tryWithIoHandling Resource.Failure(
            ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
        )
        return@tryWithIoHandling when (response.code) {
            200 -> Resource.Success(
                converter.fromJson<DefaultMessageDto>(json).message
            )
            else -> Resource.Failure(
                converter.fromJson<ResourceError.Default>(json)
            )
        }
    }
}