package com.hylandee.aurther

import kotlinx.serialization.json.Json
import java.net.http.HttpClient
import java.time.Duration

object Factory {
    fun getHttpClient(): HttpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3L))
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build()

    fun getJson(): Json = Json {
        ignoreUnknownKeys = true
    }
}