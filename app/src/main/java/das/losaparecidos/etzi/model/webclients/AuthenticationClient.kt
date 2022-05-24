package das.losaparecidos.etzi.model.webclients

import das.losaparecidos.etzi.model.entities.AuthUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * HTTP Client that makes petitions to the API to authenticate, retrieve access token and create users.
 */
@Singleton
class AuthenticationClient @Inject constructor() {


    /*************************************************
     **         Initialization and Installs         **
     *************************************************/

    private val httpClient = HttpClient(CIO) {

        // If return code is not a 2xx then throw an exception
        expectSuccess = true

        // Install JSON handler (allows to receive and send JSON data)
        install(ContentNegotiation) { json() }

        // Handle non 2xx status responses
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when {
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Unauthorized -> throw AuthenticationException()
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Conflict -> throw UserExistsException()
                    else -> {
                        exception.printStackTrace()
                        throw exception
                    }
                }
            }
        }
    }


    /*************************************************
     **                   Methods                   **
     *************************************************/

    @Throws(AuthenticationException::class, Exception::class)
    suspend fun authenticate(user: AuthUser) {
        val tokenInfo: TokenInfo = httpClient.submitForm(
            url = "https://api.etzi.eus/auth/token",
            formParameters = Parameters.build {
                append("grant_type", "password")
                append("username", user.ldap)
                append("password", user.password)
            }).body()

        bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken))
    }
}
