package com.example.domain.favourites

import com.example.domain.restaurant.Restaurant
import com.example.domain.user.User
import com.example.domain.utils.Resource


interface FavouritesRepository {
    suspend fun getFavoriteRestaurantsOfUser(userId: String): Resource<List<Favourite>>
    suspend fun getUsersWhoFavoriteRestaurant(restaurantId: String): Resource<List<User>>
    suspend fun addFavorite(userId: String, restaurantId: String): Resource<String>
    suspend fun removeFavorite(userId: String, restaurantId: String): Resource<String>
}
