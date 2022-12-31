package com.example.data.restaurant.remote


import com.example.data.restaurant.remote.dto.FilterRestaurantDto
import com.example.domain.restaurant.Restaurant
import com.example.domain.utils.Resource


interface RemoteRestaurantDao {
    suspend fun getAllRestaurants(): Resource<List<Restaurant>>
    suspend fun getExpensiveRestaurants(): Resource<List<Restaurant>>
    suspend fun getRestaurantById(id: String): Resource<Restaurant>
    suspend fun filterRestaurant(filterRestaurantDto: FilterRestaurantDto): Resource<List<Restaurant>>
    suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>>


}