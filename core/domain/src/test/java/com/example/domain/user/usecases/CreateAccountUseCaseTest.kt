package com.example.domain.user.usecases

import com.example.domain.user.TestUserRepo
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.validation.usecases.*
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateAccountUseCaseTest {
    private lateinit var createAccountUseCase: CreateAccountUseCase
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = TestUserRepo()
        createAccountUseCase = CreateAccountUseCase(
            userRepository = userRepository,
            validateEmail = ValidateEmail(),
            validateMobileNumber = ValidateMobileNumber(),
            validatePassword = ValidatePassword(),
            validateConfirmPassword = ValidateConfirmPassword(),
            validateUsername = ValidateUsername(),
            validateFirstName = ValidateFirstName(),
            validateLastName = ValidateLastName(),
            validateUserAddress = ValidateAddress(),
            validateProfilePic = ValidateProfilePic(),
            validateGender = ValidateGender(),
            )
    }

    @Test
    fun `When given invalid input should return failure`() = runBlocking {
        val result = createAccountUseCase(
            first_name = "",
            last_name = "",
            username = "",
            email = "",
            user_pass = "",
            confirmUserPass = "",
            gender = "",
            mobile_number = 65,
            address = "",
            profile_pic = null,
        ).last()
        assertEquals(true, result is Resource.Failure)
    }

    @Test
    fun `When creating account, should update user list and save new token`() = runBlocking {
        val repo = userRepository as TestUserRepo
        val oldToken = repo.token
        val oldList = repo.users
        val result = createAccountUseCase(
            first_name = "test",
            last_name = "test",
            username = "testingMock",
            email = "testingMock@gmail.com",
            user_pass = "Is@@cchen1234",
            confirmUserPass = "Is@@cchen1234",
            gender = "M",
            mobile_number = 6587789994,
            address = "Lorong chuan",
            profile_pic = null,
        ).toList()
        assertEquals(true, result[0] is Resource.Loading)
        assertEquals(true, result.last() is Resource.Success)
        val newToken = repo.token
        val newList = repo.users
        assertEquals(false, oldToken == newToken)
        assertEquals(false, oldList.last().user_id == newList.last().user_id)
        assertEquals("test", newList.last().username)
        assertEquals("test@gmail.com", newList.last().email)
    }
}