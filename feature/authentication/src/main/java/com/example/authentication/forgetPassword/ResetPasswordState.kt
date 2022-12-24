package com.example.authentication.forgetPassword

data class ResetPasswordState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isSent: Boolean = false,
)