package com.example.data.utils

import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError

import okio.IOException

suspend fun <T> tryWithIoHandling(
    callback: suspend () -> Resource<T>
): Resource<T> {
    return try {
        callback()
    } catch (e: IOException) {
        Resource.Failure(
            ResourceError.Default(e.message.toString())
        )
    }
}