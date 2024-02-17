package com.hylandee.aurther

import kotlinx.serialization.json.Json
import java.net.http.HttpClient

object Factory {
    fun getHttpClient(): HttpClient = HttpClient.newHttpClient()

    fun getJson(): Json = Json {
        ignoreUnknownKeys = true
    }
}