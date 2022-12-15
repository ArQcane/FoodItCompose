package com.example.data.di

import com.example.data.favourites.remote.RemoteFavoriteDao
import com.example.data.favourites.remote.RemoteFavoriteDaoImpl
import com.example.data.restaurant.remote.RemoteRestaurantDao
import com.example.data.restaurant.remote.RemoteRestaurantDaoImpl
import com.example.data.review.remote.RemoteReviewDao
import com.example.data.review.remote.RemoteReviewDaoImpl
import com.example.data.user.local.SharedPreferenceDao
import com.example.data.user.local.SharedPreferenceDaoImpl
import com.example.data.user.remote.RemoteUserDao
import com.example.data.user.remote.RemoteUserDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DaoModule {

    @Binds
    abstract fun bindsRestaurantDao(
        restaurantDaoImpl: RemoteRestaurantDaoImpl
    ): RemoteRestaurantDao

    @Binds
    abstract fun bindsRemoteUserDao(
        userDaoImpl: RemoteUserDaoImpl
    ): RemoteUserDao

    @Binds
    abstract fun bindsSharedPreferenceDao(
        sharedPreferenceDaoImpl: SharedPreferenceDaoImpl
    ): SharedPreferenceDao

    @Binds
    abstract fun bindsRemoteReviewDao(
        remoteReviewDaoImpl: RemoteReviewDaoImpl
    ): RemoteReviewDao

    @Binds
    abstract fun bindsRemoteFavoriteDao(
        remoteFavoriteDaoImpl: RemoteFavoriteDaoImpl
    ): RemoteFavoriteDao


}