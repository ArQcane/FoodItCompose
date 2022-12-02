package com.example.fooditcompose.domain.restaurant

import com.example.fooditcompose.data.restaurant.Restaurant
import com.example.fooditcompose.utils.Resource

interface RestaurantRepository {
    suspend fun getAllRestaurants(): Resource<List<Restaurant>>
}