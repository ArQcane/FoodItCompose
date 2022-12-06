package com.example.fooditcompose.data.user

import com.example.fooditcompose.data.user.local.SharedPreferenceDao
import com.example.fooditcompose.data.user.remote.RemoteUserDao
import com.example.fooditcompose.domain.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteUserDao: RemoteUserDao,
    private val sharedPreferenceDao: SharedPreferenceDao
) : UserRepository {

}