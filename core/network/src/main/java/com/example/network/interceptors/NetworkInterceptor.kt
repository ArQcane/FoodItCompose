package com.example.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.network.exceptions.NoInternetException
import com.example.network.exceptions.NoNetworkException
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkInterceptor(
    private val context: Context
): Interceptor {

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)
        return connection != null && (connection.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI
        ) || connection.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val timeOutMs = 1500
            val sock = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(socketAddress, timeOutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (!isConnected()) throw NoNetworkException()
        if (!isInternetAvailable()) throw NoInternetException()
        return chain.proceed(originalRequest)
    }
}