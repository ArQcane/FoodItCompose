package com.example.fooditcompose.data.user.local

import com.example.fooditcompose.domain.utils.Resource

interface SharedPreferenceDao {
    fun saveToken(token: String)
    fun getToken(): Resource<String>
}