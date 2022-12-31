package com.example.domain.restaurant

import com.example.domain.utils.Resource


interface RestaurantRepository {
    suspend fun getAllRestaurants(): Resource<List<Restaurant>>
    suspend fun getRestaurantById(id: String): Resource<Restaurant>
    suspend fun getExpensiveRestaurant(): Resource<List<Restaurant>>
    suspend fun filterRestaurant(
        region: String? = null,
        cuisine: String? = null,
        average_price_range: Int? = null,
        average_rating: Int? = null
    ): Resource<List<Restaurant>>

    suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>>
}