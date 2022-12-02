package com.example.fooditcompose.utils

sealed class Resource<T>{
    class Success<T>(val result: T) : Resource<T>()
    class Failure<T>(val error: String): Resource<T>()
}
