package com.example.domain.validation.usecases

import com.example.domain.utils.ResourceError
import javax.inject.Inject

class ValidateMobileNumber @Inject constructor() {
    operator fun invoke(value: Long, field: String = "mobile_number"): ResourceError.FieldErrorItem? {
        var error = "Invalid mobile number provided"
        val regex = """65[6|8|9]\d{7}${'$'}"""
        if (error.isEmpty()) error = "Mobile Number Required!"
        if (regex.toRegex().matches(value.toString())) return null
        return ResourceError.FieldErrorItem(field, error)
    }
}