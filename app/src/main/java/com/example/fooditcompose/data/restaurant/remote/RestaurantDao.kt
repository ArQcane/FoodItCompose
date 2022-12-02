package com.example.fooditcompose.data.restaurant.remote

import com.example.fooditcompose.domain.common.OkHttpDao
import com.example.fooditcompose.domain.restaurant.Restaurant
import com.example.fooditcompose.domain.utils.Resource
import com.google.gson.Gson
import okhttp3.OkHttpClient

abstract class RestaurantDao(
    okHttpClient: OkHttpClient,
    gson: Gson
) : OkHttpDao(okHttpClient, gson, "/restaurants") {
    abstract suspend fun getAllRestaurants(): Resource<List<Restaurant>>
    abstract suspend fun filterRestaurant(region: String?, cuisine: String?, average_price_range: Int?, average_rating: Int?): Resource<List<Restaurant>>
    abstract suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>>
    abstract suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>>
    abstract suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>>
}