package das.losaparecidos.etzi.app.activities.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.BiometricAuthenticationStatus
import das.losaparecidos.etzi.model.entities.AuthUser
import das.losaparecidos.etzi.model.repositories.ILoginRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


/*******************************************************************************
 ****                       Authentication View Model                       ****
 *******************************************************************************/

/**
 * Hilt ViewModel for Authentication screens.
 *
 * It has every state needed for the screens and it's in charge of updating the database.
 *
 * @property loginRepository Implementation of [ILoginRepository] that has necessary methods to save and fetch model data needed for authentication.
 */

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val loginRepository: ILoginRepository) : ViewModel() {
    /*************************************************
     **                    States                   **
     *************************************************/

    // Property that has the last logged user (if it exists else null)
    val lastLoggedUser: AuthUser? = runBlocking { return@runBlocking loginRepository.getLastLoggedUser() } // Get last logged user synchronously
    private val remindLastLoggedUser = runBlocking { return@runBlocking loginRepository.getRememberLogin() }

    // Biometric authentication state
    var biometricAuthenticationStatus: BiometricAuthenticationStatus by mutableStateOf(if (lastLoggedUser != null) BiometricAuthenticationStatus.NOT_AUTHENTICATED_YET else BiometricAuthenticationStatus.NO_CREDENTIALS)


    // Current Screen login to show
    var isLoginCorrect by mutableStateOf(true)
        private set

    var loginUsername by mutableStateOf(lastLoggedUser?.ldap ?: "")
    var loginPassword by mutableStateOf("")
    var rememberLogin by mutableStateOf(remindLastLoggedUser)


    // Property that defines if a background task that must block the UI is on course
    var backgroundBlockingTaskOnCourse: Boolean by mutableStateOf(false)


    /*************************************************
     **                    Events                   **
     *************************************************/

    //--------------   Login Events   --------------//

    /**
     * Checks if the given [authUser] is a valid user in the system.
     */
    private suspend fun checkUserLogin(authUser: AuthUser): Boolean {
        backgroundBlockingTaskOnCourse = true
        return loginRepository.authenticateUser(authUser)
    }


    /**
     * Checks credentials of [lastLoggedUser] and updates [biometricAuthenticationStatus] depending on the result.
     * Logged [AuthUser] if everything is correct or null otherwise.
     */
    suspend fun checkBiometricLogin(): AuthUser? {
        if (biometricAuthenticationStatus != BiometricAuthenticationStatus.NO_CREDENTIALS) {
            biometricAuthenticationStatus =
                try {
                    if (checkUserLogin(lastLoggedUser!!)) BiometricAuthenticationStatus.AUTHENTICATED else BiometricAuthenticationStatus.CREDENTIALS_ERROR
                } catch (e: Exception) {
                    BiometricAuthenticationStatus.ERROR
                }
        }
        return if (biometricAuthenticationStatus == BiometricAuthenticationStatus.AUTHENTICATED) lastLoggedUser else null
    }

    /**
     * TODO
     */
    suspend fun checkRemindLogin(): AuthUser? =
        try {
            if (remindLastLoggedUser && lastLoggedUser != null && checkUserLogin(lastLoggedUser)) lastLoggedUser else null
        } catch (e: Exception) {
            null
        }


    /**
     * Checks if the user defined with [loginUsername] and [loginPassword] exists and it's correct.
     *
     * @return Logged [AuthUser] if everything is correct or null otherwise.
     * @throws Exception if a non credential error occurs
     */
    @Throws(Exception::class)
    suspend fun checkUserPasswordLogin(): AuthUser? {
        val user = AuthUser(loginUsername, loginPassword)
        isLoginCorrect = checkUserLogin(user)
        return if (isLoginCorrect) user else null
    }


    // Update las logged username
    fun updateLastLoggedUsername(user: AuthUser) = runBlocking {
        loginRepository.setLastLoggedUser(user, rememberLogin)
    }
}