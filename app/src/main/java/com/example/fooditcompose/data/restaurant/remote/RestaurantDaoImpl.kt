package com.example.fooditcompose.data.restaurant.remote

import com.example.fooditcompose.data.restaurant.Restaurant
import com.example.fooditcompose.domain.common.dtos.DefaultErrorDto
import com.example.fooditcompose.domain.common.exceptions.NoNetworkException
import com.example.fooditcompose.utils.Constants.Companion.BASE_URL
import com.example.fooditcompose.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.fooditcompose.utils.Resource
import com.example.fooditcompose.utils.await
import com.example.fooditcompose.utils.decodeFromJson
import com.example.fooditcompose.utils.toJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import javax.inject.Inject

class RestaurantDaoImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
): RestaurantDao {
    companion object {
        val BASE_RESTAURANT_URL = "$BASE_URL/restaurants"
    }

    override suspend fun getAllRestaurants(): Resource<List<Restaurant>> {
        val request = Request.Builder().url(BASE_RESTAURANT_URL).build()
        return try {
            val response = okHttpClient.newCall(request).await()
            Resource.Success(
                Gson().fromJson(
                    response.body?.toJson(),
                    object : TypeToken<List<Restaurant>>() {}.type
                )
            )
        } catch (e: NoNetworkException) {
            Resource.Failure(e.message.toString())
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

    override suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>> {
        val request = Request.Builder()
            .url("BASE_RESTAURANT_URL/$restaurantName")
            .build()
        try {
            val response = okHttpClient.newCall(request).await()
            val json = response.body?.toJson()
            json ?: return Resource.Failure(UNABLE_GET_BODY_ERROR_MESSAGE)
            if (response.code == 200) return Resource.Success(
                gson.decodeFromJson(json)
            )
            val errorDto = gson.decodeFromJson<DefaultErrorDto>(json)
            return Resource.Failure(errorDto.error)
        } catch (e: IOException) {
            return Resource.Failure(e.message.toString())
        }
    }

    override suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>> {
        TODO("Not yet implemented")
    }

    override suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>> {
        TODO("Not yet implemented")
    }
}