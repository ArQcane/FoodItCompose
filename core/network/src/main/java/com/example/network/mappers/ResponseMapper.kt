package com.example.network.mappers


import com.example.network.models.TransformedResponse
import com.example.network.utils.toJson
import okhttp3.Response

suspend fun Response.toTransformedResponse() = TransformedResponse(
    json = body?.toJson(),
    responseCode = code
)