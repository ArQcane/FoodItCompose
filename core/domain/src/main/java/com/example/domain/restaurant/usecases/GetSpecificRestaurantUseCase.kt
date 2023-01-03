package com.example.domain.restaurant.usecases

import com.example.domain.favourites.FavouritesRepository
import com.example.domain.restaurant.RestaurantRepository
import com.example.domain.restaurant.TransformedRestaurant
import com.example.domain.restaurant.TransformedRestaurantAndReview
import com.example.domain.review.Review
import com.example.domain.review.ReviewRepository
import com.example.domain.review.TransformedReview
import com.example.domain.user.UserRepository
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class GetSpecificRestaurantUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUser,
    private val favouriteRepository: FavouritesRepository,
    private val reviewRepository: ReviewRepository,
    private val restaurantRepository: RestaurantRepository
) {
    operator fun invoke(
        restaurantId: String,
    ) = flow<Resource<TransformedRestaurantAndReview>> {
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
        val restaurant = restaurantRepository.getRestaurantById(restaurantId)
        if (restaurant !is Resource.Success) return@flow emit(
            Resource.Failure((restaurant as Resource.Failure).error)
        )
        val reviews = reviewRepository.getAllReviews()
        if (reviews !is Resource.Success) return@flow emit(
            Resource.Failure((reviews as Resource.Failure).error)
        )
        val transformedRestaurantsAndReview = restaurant.result.let { restaurant ->
            val reviewsOfRestaurant = reviews.result.filter { review ->
                review.idrestaurant == restaurant.restaurant_id
            }
            val transformedReviews = reviewsOfRestaurant.map { transformedReview ->
                val userObject = userRepository.getUserById(transformedReview.iduser.toString())
                if (userObject !is Resource.Success) return@flow emit(
                    Resource.Failure((userObject as Resource.Failure).error)
                )
                TransformedReview(
                    review_id = transformedReview.review_id,
                    idrestaurant = transformedReview.idrestaurant,
                    iduser = transformedReview.iduser,
                    dateposted = transformedReview.dateposted,
                    review = transformedReview.review,
                    rating = transformedReview.rating,
                    user = userObject.result,
                )
            }
            val isFavourited = favouriteRestaurants.result.map {
                it.restaurantID
            }.contains(restaurant.restaurant_id)
            TransformedRestaurantAndReview(
                id = restaurant.restaurant_id,
                name = restaurant.restaurant_name,
                avg_price = restaurant.average_price_range,
                cuisine = restaurant.cuisine,
                biography = restaurant.biography,
                opening_hours = restaurant.opening_hours,
                region = restaurant.region,
                restaurant_logo = restaurant.restaurant_logo,
                location_lat = restaurant.location_lat,
                location_long = restaurant.location_long,
                location = restaurant.location,
                restaurant_banner = restaurant.restaurant_banner,
                reviews = transformedReviews,
                isFavouriteByCurrentUser = isFavourited,
                averageRating = getAverageRating(reviewsOfRestaurant),
                ratingCount = reviewsOfRestaurant.size
            )
        }
        emit(Resource.Success(transformedRestaurantsAndReview))
    }

    private fun getAverageRating(reviews: List<Review>): Double {
        if (reviews.isEmpty()) return 0.0
        return Math.round((reviews.sumOf { review -> review.rating } / reviews.size.toDouble()) * 100)
            .toDouble() / 100
    }

    private suspend fun getUserId(): String? {
        val userResource = getCurrentLoggedInUserUseCase().last()
        if (userResource is Resource.Failure) return null
        return (userResource as Resource.Success).result.user_id.toString()
    }
}