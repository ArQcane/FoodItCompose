package com.example.test.favourites

import com.example.domain.favourites.Favourite
import com.example.domain.favourites.FavouritesRepository
import com.example.domain.user.User
import com.example.domain.utils.Resource

class TestFavouriteRepo :FavouritesRepository {
    override suspend fun getFavoriteRestaurantsOfUser(userId: String): Resource<List<Favourite>> {
        return Resource.Success(emptyList())
    }

    override suspend fun getUsersWhoFavoriteRestaurant(restaurantId: String): Resource<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun addFavorite(userId: String, restaurantId: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(userId: String, restaurantId: String): Resource<String> {
        TODO("Not yet implemented")
    }
}