package com.example.domain.reviews

import com.example.domain.review.usecases.DeleteReviewUseCase
import com.example.test.review.TestReviewRepo
import com.example.test.user.TestUserRepo
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeleteReviewUseCaseTest{

    lateinit var deleteReviewUseCase: DeleteReviewUseCase
    lateinit var testReviewRepo: TestReviewRepo

    @Before
    fun setUp() {
        testReviewRepo = TestReviewRepo()
        deleteReviewUseCase = DeleteReviewUseCase(
            reviewRepository = testReviewRepo,
            userRepository = TestUserRepo()
        )
    }

    @Test
    fun `When given valid inputs, should return success resource and update list`() = runBlocking {
        val reviewDelete = testReviewRepo.reviews.last()
        val result = deleteReviewUseCase(reviewDelete.review_id.toString()).toList()
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result.last() is Resource.Success)
        assertNull(testReviewRepo.reviews.find { it.review_id == reviewDelete.review_id })
    }

    @Test
    fun `When given invalid inputs, should return failure resource`() = runBlocking {
        val result = deleteReviewUseCase("not actually existing ID").toList()
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result.last() is Resource.Failure)
    }
}