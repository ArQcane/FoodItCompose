package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke(
        email: String
    ): Flow<Resource<Unit>> = flow {

    }
}