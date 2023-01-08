package com.example.user.profile.editProfile

sealed class EditProfileEvent {
    class OnFirstNameChange(val first_name: String): EditProfileEvent()
    class OnLastNameChange(val last_name: String): EditProfileEvent()
    class OnMobileNumberChange(val mobile_number: Long): EditProfileEvent()
    class OnAddressChange(val address: String): EditProfileEvent()
    class OnProfilePicChange(val profile_pic: String): EditProfileEvent()
    object OnSubmit: EditProfileEvent()
}