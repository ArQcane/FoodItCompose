package com.example.data.review.remote

import com.example.data.common.DefaultMessageDto
import com.example.data.common.EntityCreatedDto
import com.example.data.review.remote.RemoteReviewDao
import com.example.data.review.remote.dto.CreateReviewDto
import com.example.data.review.remote.dto.UpdateReviewDto
import com.example.data.utils.tryWithIoHandling
import com.example.domain.review.Review
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError

import com.example.network.Authorization
import com.example.network.OkHttpDao
import com.example.network.converter.JsonConverter
import com.example.network.delegations.AuthorizationImpl
import com.example.network.delegations.OkHttpDaoImpl
import com.example.network.utils.Constants.Companion.UNABLE_GET_BODY_ERROR_MESSAGE
import com.example.network.utils.toJson

import okhttp3.OkHttpClient

import javax.inject.Inject

class RemoteReviewDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    converter: JsonConverter,
) : RemoteReviewDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        converter = converter,
        okHttpClient = okHttpClient,
        path = "/reviews"
    ) {

    override suspend fun getAllReviews(): Resource<List<Review>> =
        tryWithIoHandling {
            val response = get()
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun getReviewsByUser(userId: String): Resource<List<Review>> =
        tryWithIoHandling {
            val response = get(endpoint = "/userId/$userId")
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>> =
        tryWithIoHandling {
            val response = get(endpoint = "/restaurantId/$restaurantId")
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson(json)
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }

    override suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        CreateReviewDto: CreateReviewDto
    ): Resource<String> = tryWithIoHandling {
        val response = post(
            endpoint = "/addreview",
            body = CreateReviewDto.copy(idRestaurant = restaurantId, idUser = userId),
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoHandling when (response.code) {
            200 -> Resource.Success(
                converter.fromJson<EntityCreatedDto>(json).insertId
            )
            400 -> Resource.Failure(
                converter.fromJson<ResourceError.Field>(json)
            )
            else -> Resource.Failure(
                converter.fromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun updateReview(
        userId: Int,
        restaurantId: Int,
        reviewId: String,
        UpdateReviewDto: UpdateReviewDto
    ): Resource<Review> = tryWithIoHandling {
        val response = put(
            endpoint = "/updatereview/$reviewId",
            body = UpdateReviewDto.copy(idRestaurant = restaurantId, idUser = userId),
        )
        val json = response.body?.toJson()
            ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
            )
        return@tryWithIoHandling when (response.code) {
            200 -> Resource.Success(
                converter.fromJson(json)
            )
            else -> Resource.Failure(
                converter.fromJson<ResourceError.Default>(json)
            )
        }
    }

    override suspend fun deleteReview(reviewId: String): Resource<String> =
        tryWithIoHandling {
            val response = delete<Unit>(
                endpoint = "/deletereview/$reviewId",
            )
            val json = response.body?.toJson()
                ?: return@tryWithIoHandling Resource.Failure(
                    ResourceError.Default(UNABLE_GET_BODY_ERROR_MESSAGE)
                )
            return@tryWithIoHandling when (response.code) {
                200 -> Resource.Success(
                    converter.fromJson<DefaultMessageDto>(json).message
                )
                else -> Resource.Failure(
                    converter.fromJson<ResourceError.Default>(json)
                )
            }
        }
}