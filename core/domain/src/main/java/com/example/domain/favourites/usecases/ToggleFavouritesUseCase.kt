package com.example.domain.favourites.usecases

import android.util.Log
import com.example.domain.favourites.FavouritesRepository
import com.example.domain.restaurant.TransformedRestaurant
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import kotlinx.coroutines.flow.last
import javax.inject.Inject
import kotlin.reflect.*

class ToggleFavouritesUseCase @Inject constructor(
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser,
    private val favouriteRepository: FavouritesRepository,
) {
    suspend fun togglingFavouriteInDetails(restaurant: TransformedRestaurantAndReview): Resource<Unit> {
        val userId = getUserId() ?: return (
                Resource.Failure(
                    ResourceError.Default("Must be logged in to do this action")
                )
                )
        Log.d("favourited?", restaurant.isFavouriteByCurrentUser.toString())
        val resource = toggleFavorite(restaurant.isFavouriteByCurrentUser).invoke(
            userId,
            restaurant.id.toString()
        )
        if (resource !is Resource.Success) return Resource.Failure<Unit>(
            (resource as Resource.Failure).error
        )
        return Resource.Success(Unit)
    }

    suspend operator fun invoke(restaurant: TransformedRestaurant): Resource<Unit> {
        val userId = getUserId() ?: return (
                Resource.Failure(
                    ResourceError.Default("Must be logged in to do this action")
                )
                )
        Log.d("favourited?", restaurant.isFavouriteByCurrentUser.toString())
        val resource = toggleFavorite(restaurant.isFavouriteByCurrentUser).invoke(
            userId,
            restaurant.id.toString()
        )
        if (resource !is Resource.Success) return Resource.Failure<Unit>(
            (resource as Resource.Failure).error
        )
        return Resource.Success(Unit)
    }

    private fun toggleFavorite(initiallyIsFavorited: Boolean): KSuspendFunction2<String, String, Resource<String>> {
        if (initiallyIsFavorited) return favouriteRepository::removeFavorite
        return favouriteRepository::addFavorite
    }

    private suspend fun getUserId(): String? {
        val userResource = getCurrentLoggedInUserUseCase().last()
        if (userResource is Resource.Failure) return null
        return (userResource as Resource.Success).result.user_id.toString()
    }
}