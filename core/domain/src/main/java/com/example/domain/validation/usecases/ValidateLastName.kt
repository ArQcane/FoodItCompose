package com.example.domain.validation.usecases

import com.example.domain.utils.ResourceError
import javax.inject.Inject

class ValidateLastName @Inject constructor() {
    operator fun invoke(
        value: String,
        field: String = "last_name"
    ): ResourceError.FieldErrorItem? {
        if (value.isNotEmpty()) return null
        return ResourceError.FieldErrorItem(field, error = "Last Name required")
    }
}