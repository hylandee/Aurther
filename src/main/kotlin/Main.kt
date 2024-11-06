import com.hylandee.aurther.ClientCredentials
import com.hylandee.aurther.ClientCredentialsConfig
import com.hylandee.aurther.TokenScout

fun main() {
    val scout = TokenScout()
    val config = ClientCredentialsConfig(
        url = "some-url",
        credentials = ClientCredentials(
            clientId = "some-id",
            clientSecret = "some-secret"
        )
    )
    println(scout.acquire(config))
}