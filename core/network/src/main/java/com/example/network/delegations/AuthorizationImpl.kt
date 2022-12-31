package com.example.network.delegations

import com.example.network.Authorization

class AuthorizationImpl : Authorization {
    override fun createAuthorizationHeader(token: String) = mapOf(
        Authorization.AUTHORIZATION to "${Authorization.BEARER} $token"
    )
}