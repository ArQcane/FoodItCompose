package com.example.fooditcompose.data.common.delegations

import com.example.fooditcompose.data.common.Authorization

class AuthorizationImpl : Authorization {
    override fun createAuthorizationHeader(token: String) = mapOf(
        Authorization.AUTHORIZATION to "${Authorization.BEARER} $token"
    )
}