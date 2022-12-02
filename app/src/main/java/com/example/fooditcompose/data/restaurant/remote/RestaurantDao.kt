package com.example.fooditcompose.data.restaurant.remote

import com.example.fooditcompose.data.restaurant.Restaurant
import com.example.fooditcompose.utils.Resource

interface RestaurantDao {
    suspend fun getAllRestaurants(): Resource<List<Restaurant>>
    suspend fun filterRestaurant(region: String?, cuisine: String?, average_price_range: Int?, average_rating: Int?): Resource<List<Restaurant>>
    suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>>
}