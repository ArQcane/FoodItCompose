package com.example.domain.validation.usecases

import com.example.domain.utils.ResourceError
import javax.inject.Inject

class ValidateAddress @Inject constructor() {
    operator fun invoke(
        value: String,
        field: String = "address"
    ): ResourceError.FieldErrorItem? {
        if (value.isNotEmpty()) return null
        return ResourceError.FieldErrorItem(field, error = "Address required")
    }
}