package com.example.fooditcompose.di

import com.example.fooditcompose.data.restaurant.remote.RestaurantDao
import com.example.fooditcompose.data.restaurant.remote.RestaurantDaoImpl
import com.example.fooditcompose.data.user.remote.UserDao
import com.example.fooditcompose.data.user.remote.UserDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DaoModule {

    @Singleton
    @Binds
    abstract fun bindsRestaurantDao(
        restaurantDaoImpl: RestaurantDaoImpl
    ): RestaurantDao

    @Singleton
    @Binds
    abstract fun bindsUserDao(
        userDaoImpl: UserDaoImpl
    ): UserDao

}