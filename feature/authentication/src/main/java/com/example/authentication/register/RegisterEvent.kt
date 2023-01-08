package com.example.authentication.register

import android.graphics.Bitmap
import com.example.authentication.login.LoginEvent
import java.math.BigInteger

sealed class RegisterEvent{
    class OnFirstNameChange(val first_name: String): RegisterEvent()
    class OnLastNameChange(val last_name: String): RegisterEvent()
    class OnUsernameChange(val username: String): RegisterEvent()
    class OnUserPassChange(val user_pass: String): RegisterEvent()
    class OnConfirmUserPassChange(val confirmUserPass: String): RegisterEvent()
    class OnGenderChange(val gender: String): RegisterEvent()
    class OnMobileNumberChange(val mobile_number: Long): RegisterEvent()
    class OnEmailChange(val email: String): RegisterEvent()
    class OnAddressChange(val address: String): RegisterEvent()
    class OnProfilePicChange(val profile_pic: Bitmap): RegisterEvent()
    object OnSubmit: RegisterEvent()
}
