package com.example.fooditcompose.domain.restaurant

import com.example.fooditcompose.domain.utils.Resource

interface RestaurantRepository {
    suspend fun getAllRestaurants(): Resource<List<Restaurant>>
}