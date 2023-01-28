package com.example.test.restaurant

import com.example.domain.restaurant.Restaurant
import com.example.domain.restaurant.RestaurantRepository
import com.example.domain.utils.Resource

class TestRestaurantRepo : RestaurantRepository {
    override suspend fun getAllRestaurants(): Resource<List<Restaurant>> {
        return Resource.Success(emptyList())
    }

    override suspend fun getRestaurantById(id: String): Resource<Restaurant> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpensiveRestaurant(): Resource<List<Restaurant>> {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>> {
        TODO("Not yet implemented")
    }

    override suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>> {
        TODO("Not yet implemented")
    }
}