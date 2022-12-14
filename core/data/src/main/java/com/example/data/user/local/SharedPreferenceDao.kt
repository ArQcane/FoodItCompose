package com.example.data.user.local

import com.example.domain.utils.Resource

interface SharedPreferenceDao {
    fun saveToken(token: String)
    fun getToken(): Resource<String>
}