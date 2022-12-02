package com.example.fooditcompose.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.fooditcompose.data.restaurant.remote.RestaurantDao
import com.example.fooditcompose.data.restaurant.remote.RestaurantDaoImpl
import com.example.fooditcompose.domain.common.NetworkInterceptor
import com.example.fooditcompose.domain.common.exceptions.NoNetworkException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkInterceptor(
        @ApplicationContext context:Context
    ):  Interceptor = NetworkInterceptor(context)

    @Singleton
    @Provides
    fun providesOkHttpClient(
        interceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()


    @Singleton
    @Provides
    fun providesGson(): Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .create()
}