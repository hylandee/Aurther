package com.hylandee.aurther.fixture

import com.hylandee.aurther.ClientCredentials
import com.hylandee.aurther.ClientCredentialsConfig

object TestFactory {

    private const val URL: String = "https://a.url.com"
    private const val CLIENT_ID = "some-client-id"
    private const val CLIENT_SECRET = "some-client-secret"

    private fun clientCredentials(): ClientCredentials = ClientCredentials(
        clientId = CLIENT_ID,
        clientSecret = CLIENT_SECRET,
    )

    fun clientCredentialsConfig(): ClientCredentialsConfig = ClientCredentialsConfig(
        url = URL,
        credentials = clientCredentials(),
    )
}
