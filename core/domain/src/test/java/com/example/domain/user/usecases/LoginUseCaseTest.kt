package com.example.domain.user.usecases

import com.example.domain.user.usecases.LoginUseCase
import com.example.domain.user.TestUserRepo
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.domain.validation.usecases.ValidateEmail
import com.example.domain.validation.usecases.ValidatePassword
import com.example.domain.validation.usecases.ValidateUsername
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest{
    private lateinit var testUserRepo: TestUserRepo
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        testUserRepo = TestUserRepo()
        loginUseCase = LoginUseCase(
            userRepository = testUserRepo,
            validatePassword = ValidatePassword(),
            validateUsername = ValidateUsername(),
        )
    }

    @Test
    fun `When given invalid inputs should return failure resource`() = runBlocking {
        val result = loginUseCase(username = "", user_pass = "").last()
        assertEquals(true, result is Resource.Failure)
        val failure = result as Resource.Failure
        assertEquals(true, failure.error is ResourceError.Field)
        val fieldError = failure.error as ResourceError.Field
        assertEquals(2, fieldError.errors.size)
    }

    @Test
    fun `When given valid inputs should return success resource and save token`() = runBlocking {
        val oldToken = testUserRepo.token
        val username = testUserRepo.users[0].username
        val result = loginUseCase(username = username, user_pass = "asdfadfsdf").toList()
        assertEquals(true, result[0] is Resource.Loading)
        assertEquals(true, result.last() is Resource.Success)
        val savedToken = testUserRepo.token
        assertNotNull(savedToken)
        assertNotEquals(oldToken, savedToken)
    }
}