package com.example.authentication.login

data class LoginState(
    val username: String = "",
    val user_pass: String = "",
    val usernameError: String? = null,
    val userPassError: String? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
)
