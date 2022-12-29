package com.example.domain.validation.usecases

import com.example.domain.utils.ResourceError
import javax.inject.Inject

class ValidateFirstName @Inject constructor()  {
    operator fun invoke(
        value: String,
        field: String = "first_name"
    ): ResourceError.FieldErrorItem? {
        if (value.isNotEmpty()) return null
        return ResourceError.FieldErrorItem(field, error = "Username required")
    }
}