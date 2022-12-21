package com.example.domain.user.usecases

import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        first_name: String,
        last_name: String,
        username: String,
        user_pass: String,
        gender: String,
        mobile_number: Int,
        email: String,
        address: String,
        profile_pic: String? = null,
    ): Flow<Resource<Unit>> = flow {
        emit(
            Resource.Loading<Unit>(
                isLoading = true
            )
        )
        val registerResult = userRepository.register(
            first_name = first_name,
            last_name = last_name,
            username = username,
            user_pass = user_pass,
            email = email,
            mobile_number = mobile_number,
            gender = gender,
            address = address,
            profile_pic = profile_pic
        )
        when (registerResult) {
            is Resource.Success -> {
                userRepository.saveToken(registerResult.result)
                emit(Resource.Success(Unit))
            }
            is Resource.Failure -> {
                emit(Resource.Failure(registerResult.error))
            }
            else -> Unit
        }
    }
}