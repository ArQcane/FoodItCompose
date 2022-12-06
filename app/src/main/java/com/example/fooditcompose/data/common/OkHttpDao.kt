package com.example.fooditcompose.data.common

import com.example.fooditcompose.utils.Constants.Companion.BASE_URL
import com.example.fooditcompose.utils.await
import com.example.fooditcompose.utils.decodeFromJson
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

open class OkHttpDao(
    private val okHttpClient: OkHttpClient,
    protected val gson: Gson,
    private val path: String
) {
    companion object {
        val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaTypeOrNull()
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "
    }

    suspend fun get(
        endpoint: String = "/",
        headers: Map<String, String> = mapOf()
    ): Response = makeRequest(
        endpoint = endpoint,
        headers = headers,
        method = HttpMethods.GET,
    )

    suspend fun <T> post(
        endpoint: String = "/",
        body: T,
        headers: Map<String, String> = mapOf()
    ): Response = makeRequest(
        endpoint = endpoint,
        body = body,
        headers = headers,
        contentType = ContentType.JSON,
        method = HttpMethods.POST
    )

    suspend fun <T> post(
        endpoint: String = "/",
        body: T,
        file: File?,
        requestName: String,
        headers: Map<String, String> = mapOf()
    ): Response = makeRequest(
        endpoint = endpoint,
        body = body,
        headers = headers,
        contentType = ContentType.MULTIPART,
        method = HttpMethods.POST,
    )

    suspend fun <T> put(
        endpoint: String = "/",
        body: T,
        headers: Map<String, String> = mapOf()
    ): Response = makeRequest(
        endpoint = endpoint,
        body = body,
        headers = headers,
        contentType = ContentType.JSON,
        method = HttpMethods.PUT,
    )

    suspend fun <T> put(
        endpoint: String = "/",
        body: T,
        file: File?,
        requestName: String,
        headers: Map<String, String> = mapOf()
    ): Response = makeRequest(
        endpoint = endpoint,
        body = body,
        headers = headers,
        contentType = ContentType.MULTIPART,
        method = HttpMethods.PUT,
    )

    suspend fun <T> delete(
        endpoint: String = "/",
        body: T? = null,
        headers: Map<String, String> = mapOf()
    ): Response = makeRequest(
        endpoint = endpoint,
        body = body,
        headers = headers,
        contentType = ContentType.JSON,
        method = HttpMethods.DELETE,
    )

    @Throws(IllegalArgumentException::class)
    private suspend fun makeRequest(
        endpoint: String,
        method: HttpMethods,
        body: Any? = null,
        headers: Map<String, String> = mapOf(),
        contentType: ContentType? = null
    ): Response {
        val requestBody = body?.let {
            when (contentType) {
                ContentType.JSON -> createJsonRequestBody(it)
                ContentType.MULTIPART -> createMultipartRequestBody(it)
                else -> null
            }
        }
        val requestBuilder = requestBody?.let {
            when (method) {
                HttpMethods.POST -> {
                    createRequestBuilder(endpoint).post(it)
                }
                HttpMethods.PUT -> {
                    createRequestBuilder(endpoint).put(it)
                }
                HttpMethods.DELETE -> {
                    createRequestBuilder(endpoint).delete(it)
                }
                else -> null
            }
        } ?: when (method) {
            HttpMethods.GET -> {
                createRequestBuilder(endpoint).get()
            }
            HttpMethods.DELETE -> {
                createRequestBuilder(endpoint).delete()
            }
            else -> throw IllegalArgumentException(
                "Unable to create a request. Invalid body and method provided"
            )
        }
        setHeaders(requestBuilder, headers)
        return okHttpClient.newCall(requestBuilder.build()).await()
    }

    private fun createJsonRequestBody(body: Any): RequestBody =
        gson.toJson(body).toRequestBody(JSON_MEDIA_TYPE)

    private fun createMultipartRequestBody(
        body: Any,
    ): RequestBody {
        val map = gson.decodeFromJson<HashMap<String, Any>>(
            gson.toJson(body)
        )
        val multipartBuilder = MultipartBody.Builder()
        map.forEach { (key, value) ->
            multipartBuilder.addFormDataPart(key, value.toString())
        }
        return multipartBuilder.build()
    }

    private fun setHeaders(
        requestBuilder: Request.Builder,
        headers: Map<String, String> = mapOf()
    ): Request.Builder = requestBuilder.apply {
        headers.forEach { addHeader(it.key, it.value) }
    }

    private fun createRequestBuilder(endpoint: String): Request.Builder {
        val url = "$BASE_URL/$path$endpoint"
        return Request.Builder().url(url)
    }

    private enum class HttpMethods {
        GET, POST, PUT, DELETE
    }

    private enum class ContentType {
        JSON, MULTIPART
    }

}