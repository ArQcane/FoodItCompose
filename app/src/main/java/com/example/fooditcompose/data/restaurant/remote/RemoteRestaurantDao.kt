package com.example.fooditcompose.data.restaurant.remote

import com.example.fooditcompose.data.restaurant.remote.dto.FilterRestaurantDto
import com.example.fooditcompose.domain.restaurant.Restaurant
import com.example.fooditcompose.domain.utils.Resource


interface RemoteRestaurantDao {
    suspend fun getAllRestaurants(): Resource<List<Restaurant>>
    suspend fun getRestaurantById(id: String): Resource<Restaurant>
    suspend fun filterRestaurant(filterRestaurantDto: FilterRestaurantDto): Resource<List<Restaurant>>
    suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>>
    suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>>


}