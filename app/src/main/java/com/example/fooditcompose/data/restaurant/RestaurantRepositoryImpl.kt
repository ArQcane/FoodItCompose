package com.example.fooditcompose.data.restaurant

import com.example.fooditcompose.data.restaurant.remote.RemoteRestaurantDao
import com.example.fooditcompose.domain.restaurant.RestaurantRepository
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDao: RemoteRestaurantDao
) : RestaurantRepository {
    override suspend fun getAllRestaurants() = restaurantDao.getAllRestaurants()
}