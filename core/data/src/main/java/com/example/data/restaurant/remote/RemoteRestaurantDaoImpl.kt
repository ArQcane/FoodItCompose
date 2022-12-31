package com.example.data.restaurant.remote


import com.example.data.restaurant.remote.dto.FilterRestaurantDto
import com.example.data.utils.Constants.NO_RESPONSE
import com.example.data.utils.tryWithIoHandling
import com.example.domain.restaurant.Restaurant
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

class RemoteRestaurantDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson,
) : RemoteRestaurantDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        gson = gson,
        okHttpClient = okHttpClient,
        path = "/restaurants"
    ) {

    override suspend fun getAllRestaurants(): Resource<List<Restaurant>> =
        tryWithIoHandling {
            val (json, code) = get()
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

    override suspend fun getRestaurantById(id: String): Resource<Restaurant> =
        tryWithIoHandling {
            val (json, code) = get(endpoint = "/$id")
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<Restaurant>() {}.type
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

    override suspend fun filterRestaurant(
        filterRestaurantDto: FilterRestaurantDto,
    ): Resource<List<Restaurant>> =
        tryWithIoHandling {
            val (json, code) = post(
                endpoint = "/filter",
                body = filterRestaurantDto.copy()
            )
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<Restaurant>() {}.type
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

    override suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>> =
        tryWithIoHandling {
            val (json, code) = get(endpoint = "/search/$restaurantName")
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<Restaurant>() {}.type
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

    override suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>> =
        tryWithIoHandling {
            val (json, code) = get("/sort/descending")
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

    override suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>> =
        tryWithIoHandling {
            val (json, code) = get("/sort/ascending")
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
}