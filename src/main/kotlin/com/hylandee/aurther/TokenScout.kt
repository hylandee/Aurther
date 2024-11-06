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

interface ITokenScout {
    fun acquire(config: ClientCredentialsConfig): TokenResponse
}

class FakeTokenScout : ITokenScout {
    override fun acquire(config: ClientCredentialsConfig) = TokenResponse(
        accessToken = "eyJraWQiOiJwUk1kRlRSSnlnXC9uWVdGMWdieTZTU3hRQ1pcL2tQVDZjRUgrUEZmY0pxdFU9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiI3ZG05aXM5bm1kbjlkZG9icWNmZGE1dGx1YyIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoicnNcL3JzIiwiYXV0aF90aW1lIjoxNzMwODYzMTQ2LCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtd2VzdC0yLmFtYXpvbmF3cy5jb21cL3VzLXdlc3QtMl91enNRQmdwZWEiLCJleHAiOjE3MzA4NjY3NDYsImlhdCI6MTczMDg2MzE0NiwidmVyc2lvbiI6MiwianRpIjoiNzkzMzI5NmEtM2JhOS00NzliLThkOTgtODI4M2RjOTBmYmQ5IiwiY2xpZW50X2lkIjoiN2RtOWlzOW5tZG45ZGRvYnFjZmRhNXRsdWMifQ.BcDyunozXEDhAsHZqyKo8T6P8xzgJI5z5QC7C5YXHZkBDwfdeuPtq7aRpe7-nidt0LT4Ffp19elg7u1AXT1qyCbCWtOs1HgGuFpt4nZ_3oaiS6hX4MkOAGbeYahhSQ_ZQe45pbPUmNkqhYYPAT1n0t7Y_uzouxLCmwHyFBCwyIyqd6jGCI6pHT29mavcvb4Ft4vrD7Dcmm_sK8f_hXgvPSHT3qeizoPtFhixI4JuOl30v6RubN82UD94vprI2u1e-kcKtbjTbF7xbt6Kvzz6ELv5yTL5OV0w5A5cllyU9ZzV2l2xG2JPbkIS7O9VPqNgH6tGLJa9wSa3aeWjv7806g",
        tokenType = "Bearer",
        secondsToExpiry = 3600,
        refreshToken = null,
        scope = null
    )
}

class TokenScout(
    private val httpClient: HttpClient = Factory.getHttpClient(),
    private val json: Json = Factory.getJson()
) : ITokenScout {
    override fun acquire(config: ClientCredentialsConfig): TokenResponse {
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
