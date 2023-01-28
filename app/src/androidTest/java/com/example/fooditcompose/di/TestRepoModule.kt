package com.example.fooditcompose.di

import com.example.domain.favourites.FavouritesRepository
import com.example.domain.restaurant.RestaurantRepository
import com.example.domain.review.ReviewRepository
import com.example.domain.user.UserRepository
import com.example.test.favourites.TestFavouriteRepo
import com.example.test.restaurant.TestRestaurantRepo
import com.example.test.review.TestReviewRepo
import com.example.test.user.TestUserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object TestRepoModule {
    @Provides
    @Singleton
    fun providesUserRepository() = TestUserRepo() as UserRepository

    @Provides
    @Singleton
    fun providesReviewRepository() = TestReviewRepo() as ReviewRepository

    @Provides
    @Singleton
    fun providesFavouriteRepository() = TestFavouriteRepo() as FavouritesRepository

    @Provides
    @Singleton
    fun providesRestaurantRepository() = TestRestaurantRepo() as RestaurantRepository
}