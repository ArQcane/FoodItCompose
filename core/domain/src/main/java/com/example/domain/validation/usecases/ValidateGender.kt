package com.example.domain.validation.usecases

import com.example.domain.utils.ResourceError
import javax.inject.Inject

class ValidateGender @Inject constructor()  {
    operator fun invoke(
        value: String,
        field: String = "gender"
    ): ResourceError.FieldErrorItem? {
        var error = "Please identify as M or F"
        val regex = """^M|F${'$'}""".toRegex(
            RegexOption.IGNORE_CASE
        )
        if (error.isEmpty()) error = "Gender required!"
        if (regex.matches(value)) return null
        return ResourceError.FieldErrorItem(field, error)
    }
}