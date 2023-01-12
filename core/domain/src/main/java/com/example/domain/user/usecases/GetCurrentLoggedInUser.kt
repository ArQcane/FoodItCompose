package com.example.domain.user.usecases

import android.util.Log
import com.example.domain.user.User
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentLoggedInUser @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<User>> = flow {
        emit(Resource.Loading(isLoading = true))
        val tokenResource = userRepository.getToken()
        if (tokenResource !is Resource.Success) return@flow emit(
            Resource.Failure(
                (tokenResource as Resource.Failure).error
            )
        )
        Log.d("token", tokenResource.result)
        val userResource = userRepository.validateToken(token = tokenResource.result)
        if (userResource !is Resource.Success) return@flow emit(
            Resource.Failure(
                (userResource as Resource.Failure).error
            )
        )
        emit(Resource.Success(userResource.result))
    }
}