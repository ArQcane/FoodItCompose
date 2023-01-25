package com.example.domain.reviews.usecases

import com.example.domain.review.usecases.CreateReviewUseCase
import com.example.domain.reviews.TestReviewRepo
import com.example.domain.user.ReviewUser
import com.example.domain.user.TestUserRepo
import com.example.domain.user.User
import com.example.domain.user.usecases.GetCurrentLoggedInUser
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class CreateReviewUseCaseTest {
    lateinit var testReviewRepo: TestReviewRepo
    lateinit var createReviewUseCase: CreateReviewUseCase
    lateinit var closeable: AutoCloseable

    @Mock
    lateinit var getCurrentLoggedInUser: GetCurrentLoggedInUser

    @Mock
    lateinit var testUserRepo: TestUserRepo

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        testReviewRepo = TestReviewRepo()
        createReviewUseCase = CreateReviewUseCase(
            reviewRepository = testReviewRepo,
            userRepository = testUserRepo,
            getCurrentLoggedInUserUseCase = getCurrentLoggedInUser
        )
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun `When given invalid inputs should return failure resource`() = runBlocking {
        val reviewUser = ReviewUser(
            username = "test",
            profile_pic = "",
        )
        whenever(
            methodCall = testUserRepo.getUserById(
                anyString()
            )
        ).thenReturn(
            Resource.Success(
                reviewUser
            )
        )
        val user = User(
            user_id = 0,
            first_name = "test",
            last_name = "test",
            username = "test",
            gender = "F",
            mobile_number = 6587789994,
            email = "test@gmail.com",
            address = "test address",
            profile_pic = "",
        )
        whenever(getCurrentLoggedInUser()).thenReturn(flowOf(Resource.Success(user)))
        val result = createReviewUseCase("", "", 0).last()
        assertTrue(result is Resource.Failure)
        val failure = result as Resource.Failure
        assertTrue(failure.error is ResourceError.Field)
        val fieldError = failure.error as ResourceError.Field
        assertEquals(2, fieldError.errors.size)
    }

    @Test
    fun `When given valid inputs should return success`() =
        runBlocking {
            val reviewUser = ReviewUser(
                username = "test",
                profile_pic = "",
            )
            whenever(
                methodCall = testUserRepo.getUserById(
                    anyString()
                )
            ).thenReturn(
                Resource.Success(
                    reviewUser
                )
            )
            val user = User(
                user_id = 0,
                first_name = "test",
                last_name = "test",
                username = "test",
                gender = "F",
                mobile_number = 6587789994,
                email = "test@gmail.com",
                address = "test address",
                profile_pic = "",
            )
            whenever(getCurrentLoggedInUser()).thenReturn(flowOf(Resource.Success(user)))
            val result = createReviewUseCase("3", "test", 5).toList()
            assertTrue(result[0] is Resource.Loading)
            assertTrue(result.last() is Resource.Success)
            val insertedReview = (result.last() as Resource.Success).result
            println("review id: ${insertedReview.review_id}")
            println("reviewUser obj: ${reviewUser}")
            val item = testReviewRepo.reviews.find { it.review_id == insertedReview.review_id }
            assertNotNull(item)
        }
}