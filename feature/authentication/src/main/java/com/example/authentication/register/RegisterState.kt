package com.example.authentication.register

internal data class RegisterState(
    val first_name: String = "",
    val last_name: String = "",
    val username: String = "",
    val user_pass: String = "",
    val confirmUserPass: String = "",
    val gender: String = "",
    val mobile_number: String = "",
    val email: String = "",
    val address: String = "",
    val profile_pic: String = "",
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val signUpStage: SignUpStage = SignUpStage.NAME,
    val isLoading: Boolean = false,
    val isCreated: Boolean = false,
)

enum class SignUpStage {
    NAME, PASSWORD
}