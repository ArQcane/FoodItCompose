package com.example.domain.restaurant.usecases

import com.example.domain.favourites.FavouritesRepository
import com.example.domain.restaurant.RestaurantRepository
import com.example.domain.restaurant.TransformedRestaurant
import com.example.domain.review.Review
import com.example.domain.review.ReviewRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

import javax.inject.Inject

class GetAllRestaurantsUseCase @Inject constructor(
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser,
    private val favouriteRepository: FavouritesRepository,
    private val reviewRepository: ReviewRepository,
    private val restaurantRepository: RestaurantRepository
) {
    operator fun invoke() = flow<Resource<List<TransformedRestaurant>>> {
        emit(Resource.Loading(isLoading = true))
        val userId = getUserId() ?: return@flow emit(
            Resource.Failure(
                ResourceError.Default("Must be logged in to do this action")
            )
        )
        val favouriteRestaurants = favouriteRepository.getFavoriteRestaurantsOfUser(
            userId = userId
        )
        if (favouriteRestaurants !is Resource.Success) return@flow emit(
            Resource.Failure((favouriteRestaurants as Resource.Failure).error)
        )
        val restaurants = restaurantRepository.getAllRestaurants()
        if (restaurants !is Resource.Success) return@flow emit(
            Resource.Failure((restaurants as Resource.Failure).error)
        )
        val reviews = reviewRepository.getAllReviews()
        if (reviews !is Resource.Success) return@flow emit(
            Resource.Failure((reviews as Resource.Failure).error)
        )
        val transformedRestaurants = restaurants.result.map {
            val reviewsOfRestaurant = reviews.result.filter { review ->
                review.idrestaurant == it.restaurant_id
            }
            val isFavourited = favouriteRestaurants.result.map { restaurant ->
                restaurant.restaurant_id
            }.contains(it.restaurant_id)
            TransformedRestaurant(
                id = it.restaurant_id,
                name = it.restaurant_name,
                avg_price = it.average_price_range,
                cuisine = it.cuisine,
                biography = it.biography,
                opening_hours = it.opening_hours,
                region = it.region,
                restaurant_logo = it.restaurant_logo,
                location_lat = it.location_lat,
                location_long = it.location_long,
                location = it.location,
                restaurant_banner = it.restaurant_banner,
                reviews = reviewsOfRestaurant,
                isFavouriteByCurrentUser = isFavourited,
                averageRating = getAverageRating(reviewsOfRestaurant),
                ratingCount = reviewsOfRestaurant.size
            )
        }
        emit(Resource.Success(transformedRestaurants))
    }

    private fun getAverageRating(reviews: List<Review>): Double {
        if (reviews.isEmpty()) return 0.0
        return reviews.sumOf { review -> review.rating } / reviews.size.toDouble()
    }

    private suspend fun getUserId(): String? {
        val userResource = getCurrentLoggedInUserUseCase().last()
        if (userResource is Resource.Failure) return null
        return (userResource as Resource.Success).result.user_id.toString()
    }
}