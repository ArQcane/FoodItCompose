package com.example.fooditcompose.data.favourites.remote

import OkHttpDao
import com.example.fooditcompose.data.common.Authorization
import com.example.fooditcompose.data.common.converter.JsonConverter
import com.example.fooditcompose.data.common.delegations.AuthorizationImpl
import com.example.fooditcompose.data.common.delegations.OkHttpDaoImpl
import com.example.fooditcompose.data.common.dtos.DefaultMessageDto
import com.example.fooditcompose.data.utils.tryWithIoHandling
import com.example.fooditcompose.domain.restaurant.Restaurant
import com.example.fooditcompose.domain.user.User
import com.example.fooditcompose.domain.utils.Resource
import com.example.fooditcompose.domain.utils.ResourceError
import com.example.fooditcompose.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.fooditcompose.utils.toJson
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