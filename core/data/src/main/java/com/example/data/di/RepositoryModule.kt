package com.example.data.di

import com.example.data.favourites.FavouritesRepositoryImpl
import com.example.data.restaurant.RestaurantRepositoryImpl
import com.example.data.review.ReviewRepositoryImpl
import com.example.data.user.UserRepositoryImpl
import com.example.domain.favourites.FavouritesRepository
import com.example.domain.restaurant.RestaurantRepository
import com.example.domain.review.ReviewRepository
import com.example.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Binds
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository


    @Binds
    abstract fun bindsReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    abstract fun bindsFavouriteRepository(
        favoriteRepositoryImpl: FavouritesRepositoryImpl
    ): FavouritesRepository
}