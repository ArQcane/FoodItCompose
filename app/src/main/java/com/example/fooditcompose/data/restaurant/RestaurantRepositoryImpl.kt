package com.example.fooditcompose.data.restaurant

import com.example.fooditcompose.data.restaurant.remote.RestaurantDao
import com.example.fooditcompose.domain.restaurant.RestaurantRepository
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDao: RestaurantDao
) : RestaurantRepository {
    override suspend fun getAllRestaurants() = restaurantDao.getAllRestaurants()
}