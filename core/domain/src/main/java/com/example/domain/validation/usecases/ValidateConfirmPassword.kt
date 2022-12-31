package com.example.domain.validation.usecases

import com.example.domain.utils.ResourceError
import javax.inject.Inject

class ValidateConfirmPassword @Inject constructor() {
    operator fun invoke(
        value: String,
        user_pass: String,
        field: String = "confirmUserPass",
    ): ResourceError.FieldErrorItem? {
        if (user_pass == value) return null
        return ResourceError.FieldErrorItem(
            field,
            error = "Passwords do not match!"
        )
    }
}