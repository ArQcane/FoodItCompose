package com.example.domain.validation.usecases

import com.example.domain.utils.ResourceError
import javax.inject.Inject


class ValidatePassword @Inject constructor() {
    operator fun invoke(
        value: String,
        flag: String = CREATE_FLAG,
        field: String = "user_pass"
    ): ResourceError.FieldErrorItem? {
        val regex = """^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@${'$'}%^&*-]).{8,}${'$'}"""
        val error: String
        val isValidated: Boolean
        when (flag) {
            CREATE_FLAG -> {
                isValidated = regex.toRegex().matches(value)
                error = "One of the requirements is not met"
            }
            LOGIN_FLAG -> {
                isValidated = value.isNotEmpty() && value.isNotBlank()
                error = "Password required!"
            }
            else -> throw IllegalArgumentException("Invalid flag provided")
        }
        if (isValidated) return null
        return ResourceError.FieldErrorItem(field, error)
    }

    companion object {
        const val CREATE_FLAG = "CREATE_FLAG"
        const val LOGIN_FLAG = "LOGIN_FLAG"
    }
}