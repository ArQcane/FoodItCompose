package com.example.domain.restaurant.usecases

import com.example.domain.favourites.FavouritesRepository
import com.example.domain.restaurant.RestaurantRepository
import com.example.domain.restaurant.TransformedRestaurant
import com.example.domain.review.Review
import com.example.domain.review.ReviewRepository
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetUserFromTokenUseCase
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.w3c.dom.Comment
import javax.inject.Inject

class GetAllRestaurantsUseCase @Inject constructor(
    private val getUserFromTokenUseCase: GetUserFromTokenUseCase,
    private val favouriteRepository: FavouritesRepository,
    private val reviewRepository: ReviewRepository,
    private val restaurantRepository: RestaurantRepository
) {
    operator fun invoke() = flow<Resource<List<TransformedRestaurant>>> {
        emit(Resource.Loading(isLoading = true))
        getUserFromTokenUseCase().collect { userResource ->
            if (userResource is Resource.Loading) return@collect emit(
                Resource.Loading(isLoading = true)
            )
            if (userResource is Resource.Failure) return@collect emit(
                Resource.Failure(userResource.error)
            )
            val userId = (userResource as Resource.Success).result.user_id
            val favouriteRestaurants = favouriteRepository.getFavoriteRestaurantsOfUser(
                userId = userId.toString()
            )
            if (favouriteRestaurants !is Resource.Success) return@collect emit(
                Resource.Failure((favouriteRestaurants as Resource.Failure).error)
            )
            val restaurants = restaurantRepository.getAllRestaurants()
            if (restaurants !is Resource.Success) return@collect emit(
                Resource.Failure((restaurants as Resource.Failure).error)
            )
            val reviews = reviewRepository.getAllReviews()
            if (reviews !is Resource.Success) return@collect emit(
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
    }

    private fun getAverageRating(reviews: List<Review>): Double {
        if (reviews.isEmpty()) return 0.0
        return reviews.sumOf { review -> review.rating } / reviews.size.toDouble()
    }
}