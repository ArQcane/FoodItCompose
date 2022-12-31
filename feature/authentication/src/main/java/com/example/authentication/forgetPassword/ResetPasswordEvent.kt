package com.example.authentication.forgetPassword

sealed class ResetPasswordEvent {
    class OnEmailChange(val email: String): ResetPasswordEvent()
    object OnSubmit: ResetPasswordEvent()
}