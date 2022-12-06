package com.example.fooditcompose.data.restaurant.remote

import com.example.fooditcompose.data.utils.tryWithIoExceptionHandling
import com.example.fooditcompose.domain.restaurant.Restaurant
import com.example.fooditcompose.domain.utils.Resource
import com.example.fooditcompose.domain.utils.ResourceError
import com.example.fooditcompose.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.fooditcompose.utils.decodeFromJson
import com.example.fooditcompose.utils.toJson
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okio.IOException
import javax.inject.Inject

class RemoteRestaurantDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson,
) : RemoteRestaurantDao(okHttpClient, gson) {
    override suspend fun getAllRestaurants(): Resource<List<Restaurant>> =
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

    override suspend fun getRestaurantById(id: String): Resource<Restaurant> =
        tryWithIoExceptionHandling {
            val response = get(endpoint = "/id/$id")
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

    override suspend fun filterRestaurant(
        region: String?,
        cuisine: String?,
        average_price_range: Int?,
        average_rating: Int?
    ): Resource<List<Restaurant>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>> =
        tryWithIoExceptionHandling {
            val response = get(endpoint = "/search/$restaurantName")
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

    override suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>> =
        tryWithIoExceptionHandling {
            val response = get(endpoint = "/sort/descending")
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

    override suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>> =
        tryWithIoExceptionHandling {
            val response = get(endpoint = "/sort/ascending")
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
}