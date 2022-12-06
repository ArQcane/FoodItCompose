package com.example.fooditcompose.di

import com.example.fooditcompose.data.restaurant.RestaurantRepositoryImpl
import com.example.fooditcompose.data.review.ReviewRepositoryImpl
import com.example.fooditcompose.data.user.UserRepositoryImpl
import com.example.fooditcompose.domain.restaurant.RestaurantRepository
import com.example.fooditcompose.domain.review.ReviewRepository
import com.example.fooditcompose.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Singleton
    @Binds
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindsReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository
}