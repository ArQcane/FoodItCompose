package com.example.authentication.login

sealed class LoginEvent{
    class OnUsernameChange(val username: String): LoginEvent()
    class OnUserPassChange(val user_pass: String): LoginEvent()
    object OnSubmit: LoginEvent()
}
