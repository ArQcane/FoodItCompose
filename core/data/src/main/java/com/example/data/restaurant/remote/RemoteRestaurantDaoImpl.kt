package com.example.data.restaurant.remote


import com.example.data.restaurant.remote.dto.FilterRestaurantDto
import com.example.data.utils.tryWithIoHandling
import com.example.domain.restaurant.Restaurant
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
import okio.IOException
import javax.inject.Inject

class RemoteRestaurantDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    converter: JsonConverter,
) : RemoteRestaurantDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        converter = converter,
        okHttpClient = okHttpClient,
        path = "/restaurants"
    ) {

    override suspend fun getAllRestaurants(): Resource<List<Restaurant>> =
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

    override suspend fun getRestaurantById(id: String): Resource<Restaurant> =
        tryWithIoHandling {
            val response = get(endpoint = "/$id")
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

    override suspend fun filterRestaurant(
        filterRestaurantDto: FilterRestaurantDto,
    ): Resource<List<Restaurant>> {
        try {
            val response = post(
                endpoint = "/filter",
                body = filterRestaurantDto.copy()
            )
            val json = response.body?.toJson() ?: return Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
            return when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }

    override suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>> {
        try {
            val response = get(endpoint = "/search/$restaurantName")
            val json = response.body?.toJson() ?: return Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
            return when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }

    override suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>> {
        try {
            val response = get("/sort/descending")
            val json = response.body?.toJson() ?: return Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
            return when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }

    override suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>> {
        try {
            val response = get("/sort/ascending")
            val json = response.body?.toJson() ?: return Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
            return when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        } catch (e: IOException) {
            return Resource.Failure(
                ResourceError.Default(e.message.toString())
            )
        }
    }
}