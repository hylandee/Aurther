package com.hylandee.aurther

import com.hylandee.aurther.fixture.TestFactory
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.mockk.every
import io.mockk.mockk

import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandler

class TokenScoutTest : ShouldSpec({

    val httpClient = mockk<HttpClient>(relaxed = true)
    val httpResponse = mockk<HttpResponse<String>>(relaxed = true) {
        every { body() } returns """
        {
            "access_token": "garble-de-gook",
            "token_type": "Bearer"
            "expires_in": 3600
        }
       """.trimIndent()
    }
    val tokenScout = TokenScout(httpClient = httpClient)

    should("get a token") {
        // given
        every { httpClient.send(any(), any<BodyHandler<String>>()) } returns httpResponse

        // when
        val actual = tokenScout.acquire(TestFactory.clientCredentialsConfig())

        // then
        actual shouldBe TokenResponse(
            accessToken = "garble-de-gook",
            tokenType = "Bearer",
            secondsToExpiry = 3600L
        )
    }

    should("get a token from the fake scout") {
        // given
        val scout = FakeTokenScout()

        // when
        val response = scout.acquire(TestFactory.clientCredentialsConfig())

        // then
        response.refreshToken.shouldBeNull()
        response.tokenType shouldBe "Bearer"
        response.accessToken.shouldNotBeBlank()
        response.scope.shouldBeNull()
        response.secondsToExpiry shouldBe 3600L
    }
})
