package com.example.fooditcompose.data.favourites.remote

import com.example.fooditcompose.domain.restaurant.Restaurant
import com.example.fooditcompose.domain.user.User
import com.example.fooditcompose.domain.utils.Resource

interface RemoteFavoriteDao {
    suspend fun getFavoriteRestaurantsOfUser(userId: String): Resource<List<Restaurant>>
    suspend fun getUsersWhoFavoriteRestaurant(restaurantId: String): Resource<List<User>>
    suspend fun addFavorite(userId: String, restaurantId: String): Resource<String>
    suspend fun removeFavorite(userId: String, restaurantId: String): Resource<String>
}