package com.example.fooditcompose.di

import android.content.Context
import com.example.fooditcompose.BuildConfig
import com.example.fooditcompose.data.restaurant.RestaurantRepositoryImpl
import com.example.fooditcompose.data.restaurant.remote.RestaurantDao
import com.example.fooditcompose.data.user.UserRepositoryImpl
import com.example.fooditcompose.domain.restaurant.RestaurantRepository
import com.example.fooditcompose.domain.user.UserRepository
import com.example.fooditcompose.utils.Constants.Companion.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
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
}