package com.example.fooditcompose.data.review.remote

import com.example.fooditcompose.data.common.dtos.DefaultMessageDto
import com.example.fooditcompose.data.common.dtos.EntityCreatedDto
import com.example.fooditcompose.data.review.remote.dto.CreateReviewDto
import com.example.fooditcompose.data.review.remote.dto.UpdateReviewDto
import com.example.fooditcompose.data.utils.tryWithIoExceptionHandling
import com.example.fooditcompose.domain.review.Review
import com.example.fooditcompose.domain.utils.Resource
import com.example.fooditcompose.domain.utils.ResourceError
import com.example.fooditcompose.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.fooditcompose.utils.decodeFromJson
import com.example.fooditcompose.utils.toJson
import com.google.gson.Gson
import okhttp3.OkHttpClient

class RemoteReviewDaoImpl(
    okHttpClient: OkHttpClient,
    gson: Gson,
): RemoteReviewDao(okHttpClient, gson) {
    override suspend fun getAllReviews(): Resource<List<Review>>  =
        tryWithIoExceptionHandling {
            val response = get()
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun getReviewsByUser(userId: String): Resource<List<Review>>  =
        tryWithIoExceptionHandling {
            val response = get(endpoint = "/userId/$userId")
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>>  =
        tryWithIoExceptionHandling {
            val response = get(endpoint = "/restaurantId/$restaurantId")
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson(json)
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        createReviewDto: CreateReviewDto
    ): Resource<String>  = tryWithIoExceptionHandling {
        val response = post(
            endpoint = "/addreview",
            body = createReviewDto.copy(idRestaurant = restaurantId, idUser = userId),
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoExceptionHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoExceptionHandling when (response.code) {
            200 -> Resource.Success(
                gson.decodeFromJson<EntityCreatedDto>(json).insertId
            )
            400 -> Resource.Failure(
                gson.decodeFromJson<ResourceError.Field>(json)
            )
            else -> Resource.Failure(
                gson.decodeFromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun updateReview(
        userId: Int,
        restaurantId: Int,
        reviewId: String,
        updateReviewDto: UpdateReviewDto
    ): Resource<Review> = tryWithIoExceptionHandling {
        val response = put(
            endpoint = "/updatereview/$reviewId",
            body = updateReviewDto.copy(idRestaurant = restaurantId, idUser = userId)
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoExceptionHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoExceptionHandling when (response.code) {
            200 -> Resource.Success(
                gson.decodeFromJson(json)
            )
            else -> Resource.Failure(
                gson.decodeFromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun deleteReview(reviewId: String): Resource<String> =
        tryWithIoExceptionHandling {
            val response = delete<Unit>(
                endpoint = "/deletereview/$reviewId",
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoExceptionHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoExceptionHandling when (response.code) {
                200 -> Resource.Success(
                    gson.decodeFromJson<DefaultMessageDto>(json).message
                )
                else -> Resource.Failure(
                    gson.decodeFromJson<ResourceError.Default>(json)
                )
            }
        }
}