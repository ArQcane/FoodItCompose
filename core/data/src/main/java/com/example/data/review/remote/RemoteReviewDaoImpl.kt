package com.example.data.review.remote

import com.example.data.common.DefaultMessageDto
import com.example.data.common.EntityCreatedDto
import com.example.data.review.remote.dto.CreateReviewDto
import com.example.data.review.remote.dto.UpdateReviewDto
import com.example.data.utils.Constants.NO_RESPONSE
import com.example.data.utils.tryWithIoHandling
import com.example.domain.review.Review
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError

import com.example.network.Authorization
import com.example.network.OkHttpDao
import com.example.network.delegations.AuthorizationImpl
import com.example.network.delegations.OkHttpDaoImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import okhttp3.OkHttpClient

import javax.inject.Inject

class RemoteReviewDaoImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson
) : RemoteReviewDao,
    Authorization by AuthorizationImpl(),
    OkHttpDao by OkHttpDaoImpl(
        gson = gson,
        okHttpClient = okHttpClient,
        path = "/reviews"
    ) {

    override suspend fun getAllReviews(): Resource<List<Review>> =
        tryWithIoHandling {
            val (json, code) = get()
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<List<Review>>() {}.type
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

    override suspend fun getReviewsByUser(userId: String): Resource<List<Review>> =
        tryWithIoHandling {
            val (json,code) = get(endpoint = "/userId/$userId")
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<List<Review>>() {}.type
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

    override suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>> =
        tryWithIoHandling {
            val (json,code) = get(endpoint = "/restaurantId/$restaurantId")
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson(
                        json,
                        object : TypeToken<List<Review>>() {}.type
                    )
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }

    override suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        CreateReviewDto: CreateReviewDto
    ): Resource<String> = tryWithIoHandling {
        val (json,code) = post(
            endpoint = "/addreview",
            body = CreateReviewDto.copy(idRestaurant = restaurantId, idUser = userId),
        )
        json ?: return@tryWithIoHandling Resource.Failure(
            ResourceError.Default(NO_RESPONSE)
        )
        return@tryWithIoHandling when (code) {
            200 -> Resource.Success(
                gson.fromJson<EntityCreatedDto>(
                    json,
                    object : TypeToken<EntityCreatedDto>() {}.type
                ).insertId
            )
            400 -> Resource.Failure(
                gson.fromJson<ResourceError.Field>(
                    json,
                    object : TypeToken<ResourceError.Field>() {}.type
                )
            )
            else -> Resource.Failure(
                gson.fromJson<ResourceError.Default>(
                    json,
                    object : TypeToken<ResourceError.Default>() {}.type
                )
            )
        }
    }

    override suspend fun updateReview(
        userId: Int,
        restaurantId: Int,
        reviewId: String,
        UpdateReviewDto: UpdateReviewDto
    ): Resource<Review> = tryWithIoHandling {
        val (json,code) = put(
            endpoint = "/updatereview/$reviewId",
            body = UpdateReviewDto.copy(idRestaurant = restaurantId, idUser = userId),
        )
        json ?: return@tryWithIoHandling Resource.Failure(
            ResourceError.Default(NO_RESPONSE)
        )
        return@tryWithIoHandling when (code) {
            200 -> Resource.Success(
                gson.fromJson(
                    json,
                    object : TypeToken<Review>() {}.type
                )
            )
            else -> Resource.Failure(
                gson.fromJson<ResourceError.Default>(
                    json,
                    object : TypeToken<ResourceError.Default>() {}.type
                )
            )
        }
    }

    override suspend fun deleteReview(reviewId: String): Resource<String> =
        tryWithIoHandling {
            val (json,code) = delete<Unit>(
                endpoint = "/deletereview/$reviewId",
            )
            json ?: return@tryWithIoHandling Resource.Failure(
                ResourceError.Default(NO_RESPONSE)
            )
            return@tryWithIoHandling when (code) {
                200 -> Resource.Success(
                    gson.fromJson<DefaultMessageDto>(
                        json,
                        object : TypeToken<DefaultMessageDto>() {}.type
                    ).message
                )
                else -> Resource.Failure(
                    gson.fromJson<ResourceError.Default>(
                        json,
                        object : TypeToken<ResourceError.Default>() {}.type
                    )
                )
            }
        }
}