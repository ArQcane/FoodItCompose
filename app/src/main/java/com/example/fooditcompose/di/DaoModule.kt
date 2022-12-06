package com.example.fooditcompose.di

import com.example.fooditcompose.data.restaurant.remote.RemoteRestaurantDao
import com.example.fooditcompose.data.restaurant.remote.RemoteRestaurantDaoImpl
import com.example.fooditcompose.data.user.local.SharedPreferenceDao
import com.example.fooditcompose.data.user.local.SharedPreferenceDaoImpl
import com.example.fooditcompose.data.user.remote.RemoteUserDao
import com.example.fooditcompose.data.user.remote.RemoteUserDaoImpl
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
        restaurantDaoImpl: RemoteRestaurantDaoImpl
    ): RemoteRestaurantDao

    @Singleton
    @Binds
    abstract fun bindsRemoteUserDao(
        userDaoImpl: RemoteUserDaoImpl
    ): RemoteUserDao

    @Binds
    abstract fun bindsSharedPreferenceDao(
        sharedPreferenceDaoImpl: SharedPreferenceDaoImpl
    ): SharedPreferenceDao

}