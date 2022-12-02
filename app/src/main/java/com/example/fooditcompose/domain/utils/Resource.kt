package com.example.fooditcompose.domain.utils

sealed class Resource<Result>() {
    class Success<Result>(val result: Result) : Resource<Result>()
    class Failure<Result>(val error: ResourceError) : Resource<Result>()
}