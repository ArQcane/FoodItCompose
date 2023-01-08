package com.example.domain.validation.usecases

import android.graphics.Bitmap
import com.example.domain.utils.ResourceError
import javax.inject.Inject

class ValidateProfilePic @Inject constructor() {
    operator fun invoke(
        value: Bitmap?,
        field: String = "profile_pic"
    ): ResourceError.FieldErrorItem? {
        if (value != null) return null
        return ResourceError.FieldErrorItem(field, error = "Profile Picture required")
    }
}