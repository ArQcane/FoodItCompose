package com.example.network.converter

interface JsonConverter {
    fun <T> toJson(src: T): String
    fun <T> fromJson(json: String): T
}