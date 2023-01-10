package com.example.data.favourites.remote

import com.example.domain.favourites.Favourite
import com.example.domain.user.User
import com.example.domain.utils.Resource

interface RemoteFavoriteDao {
    suspend fun getFavoriteRestaurantsOfUser(userId: String): Resource<List<Favourite>>
    suspend fun getUsersWhoFavoriteRestaurant(restaurantId: String): Resource<List<User>>
    suspend fun addFavorite(userId: String, restaurantId: String): Resource<String>
    suspend fun removeFavorite(userId: String, restaurantId: String): Resource<String>
}