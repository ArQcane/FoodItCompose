package com.example.data.restaurant

import com.example.data.restaurant.remote.RemoteRestaurantDao
import com.example.data.restaurant.remote.dto.FilterRestaurantDto
import com.example.domain.restaurant.Restaurant
import com.example.domain.restaurant.RestaurantRepository
import com.example.domain.utils.Resource
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDao: RemoteRestaurantDao
) : RestaurantRepository {
    override suspend fun getAllRestaurants() = restaurantDao.getAllRestaurants()
    override suspend fun getRestaurantById(id: String): Resource<Restaurant> =
        restaurantDao.getRestaurantById(id = id)

    override suspend fun filterRestaurant(
        region: String?,
        cuisine: String?,
        average_price_range: Int?,
        average_rating: Int?
    ): Resource<List<Restaurant>> =
        restaurantDao.filterRestaurant(
            filterRestaurantDto = FilterRestaurantDto(
                region = region,
                cuisine = cuisine,
                average_price_range = average_price_range,
                average_rating = average_rating
            )
        )

    override suspend fun searchRestaurant(restaurantName: String?): Resource<List<Restaurant>> =
        restaurantDao.searchRestaurant(restaurantName = restaurantName)

    override suspend fun sortRestaurantsByDescendingRating(): Resource<List<Restaurant>> =
        restaurantDao.sortRestaurantsByDescendingRating()

    override suspend fun sortRestaurantsByAscendingRating(): Resource<List<Restaurant>> =
        restaurantDao.sortRestaurantsByAscendingRating()
}