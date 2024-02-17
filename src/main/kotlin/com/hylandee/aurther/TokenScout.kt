package com.hylandee.aurther

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.util.Base64

class TokenScout(
    private val httpClient: HttpClient = Factory.getHttpClient(),
    private val json: Json = Factory.getJson()
) {
    fun acquire(config: ClientCredentialsConfig): TokenResponse {
        val uriBuilder = StringBuilder(config.url)
            .append("?grant_type=client_credentials")
            .append("&scope=${config.scopes.joinToString(" ")}")
        val request = HttpRequest.newBuilder(URI(uriBuilder.toString()))
            .POST(BodyPublishers.noBody())
            .headers(
                "Authorization", config.credentials.toAuthHeader(),
                "Content-Type", "application/x-www-form-urlencoded"
            ).build()
        val response: HttpResponse<String> = httpClient.send(request, BodyHandlers.ofString())
        return json.decodeFromString(response.body())
    }
}

data class ClientCredentialsConfig(
    val url: String,
    val credentials: ClientCredentials,
    val scopes: List<String> = listOf(),
)

data class ClientCredentials(
    val clientId: String,
    val clientSecret: String,
) {
    fun toAuthHeader(): String =
        "Basic ${Base64.getEncoder().encodeToString("${this.clientId}:${this.clientSecret}".toByteArray())}"
}

@Serializable
data class TokenResponse(

    @SerialName("access_token")
    val accessToken: String,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("expires_in")
    val secondsToExpiry: Long? = null,

    @SerialName("refresh_token")
    val refreshToken: String? = null,

    val scope: String? = null,
)
