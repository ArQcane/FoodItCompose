package com.example.data.favourites

import com.example.data.favourites.remote.RemoteFavoriteDao
import com.example.domain.favourites.Favourite
import com.example.domain.favourites.FavouritesRepository
import com.example.domain.restaurant.Restaurant
import com.example.domain.user.User
import com.example.domain.utils.Resource
import javax.inject.Inject

class FavouritesRepositoryImpl @Inject constructor(
    private val remoteFavoriteDao: RemoteFavoriteDao,
): FavouritesRepository {
    override suspend fun getFavoriteRestaurantsOfUser(userId: String): Resource<List<Favourite>> =
        remoteFavoriteDao.getFavoriteRestaurantsOfUser(userId)

    override suspend fun getUsersWhoFavoriteRestaurant(restaurantId: String): Resource<List<com.example.domain.user.User>> =
        remoteFavoriteDao.getUsersWhoFavoriteRestaurant(restaurantId)

    override suspend fun addFavorite(userId: String, restaurantId: String): Resource<String> =
        remoteFavoriteDao.addFavorite(userId = userId, restaurantId = restaurantId)

    override suspend fun removeFavorite(userId: String, restaurantId: String): Resource<String> =
        remoteFavoriteDao.removeFavorite(userId= userId, restaurantId = restaurantId)
}