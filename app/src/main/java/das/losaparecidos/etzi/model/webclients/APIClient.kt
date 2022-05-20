package das.losaparecidos.etzi.model.webclients

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import das.losaparecidos.etzi.model.entities.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton


/**
 * HTTP Client that makes authenticated petitions to REST API.
 *
 * It manages automatic access token refresh.
 */
@Singleton
class APIClient @Inject constructor() {

    /*************************************************
     **         Initialization and Installs         **
     *************************************************/

    private val httpClient = HttpClient(CIO) {

        // If return code is not a 2xx then throw an exception
        expectSuccess = true

        // Install JSON handler (allows to receive and send JSON data)
        install(ContentNegotiation) { json() }

        // Install Bearer Authentication Handler
        install(Auth) {
            bearer {

                // Define where to get tokens from
                loadTokens { bearerTokenStorage.last() }

                // Send always the token, do not  wait for a 401 before adding the token to the header
                sendWithoutRequest { request -> request.url.host == "api.etzi.eus" }

                // Define token refreshing flow
                refreshTokens {

                    // Get the new token
                    val refreshTokenInfo: TokenInfo = client.submitForm(
                        url = "https://api.etzi.eus/auth/refresh",
                        formParameters = Parameters.build {
                            append("grant_type", "refresh_token")
                            append("refresh_token", oldTokens?.refreshToken ?: "")
                        }
                    ) { markAsRefreshTokenRequest() }.body()

                    // Add tokens to Token Storage and return the newest one
                    bearerTokenStorage.add(BearerTokens(refreshTokenInfo.accessToken, oldTokens?.refreshToken!!))
                    bearerTokenStorage.last()
                }
            }
        }
    }


    /*************************************************
     **                   Methods                   **
     *************************************************/

    //--------   User subscription to FCM   --------//

    suspend fun subscribeUser(FCMClientToken: String) {
        httpClient.post("https://api.etzi.eus/notifications/subscribe") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("fcm_client_token" to FCMClientToken))
        }
    }


    //----------   User's profile image   ----------//

    suspend fun getUserProfile(): Bitmap {
        val response = httpClient.get("https://api.etzi.eus/profile/image")
        val image: ByteArray = response.body()
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    suspend fun uploadUserProfile(image: Bitmap) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        httpClient.submitFormWithBinaryData(
            url = "https://api.etzi.eus/profile/image",
            formData = formData {
                append("file", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=profile_image.png")
                })
            }
        ) { method = HttpMethod.Put }
    }


    //--------------   Student Data   --------------//

    suspend fun getStudentData(): Student = httpClient.get("https://api.etzi.eus/student").body()

    suspend fun getTimetable(): List<Lecture> {
        val response: List<SerializableLecture> = httpClient.get("https://api.etzi.eus/student/timetable").body()
        return response.map(SerializableLecture::lecture)
    }

    suspend fun getTutorials(): List<SubjectTutorial> =
        httpClient.get("https://api.etzi.eus/student/tutorials").body()

    suspend fun getRecord(): List<SubjectEnrollment> = httpClient.get("https://api.etzi.eus/student/record").body()
}
