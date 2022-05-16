package das.losaparecidos.etzi.app.activities.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.BiometricAuthManager
import das.losaparecidos.etzi.model.entities.AuthUser
import das.losaparecidos.etzi.model.webclients.APIClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {

    /*************************************************
     **     ViewModels and other manager classes    **
     *************************************************/

    @Inject
    lateinit var httpClient: APIClient
    private val authViewModel: AuthenticationViewModel by viewModels()
    private lateinit var biometricAuthManager: BiometricAuthManager


    /*************************************************
     **          Activity Lifecycle Methods         **
     *************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize biometric authentication manager (only if it is uninitialized)
        if (!this::biometricAuthManager.isInitialized) {
            biometricAuthManager = BiometricAuthManager(
                context = this,
                authUsername = authViewModel.lastLoggedUser?.ldap ?: "",
                onAuthenticationSucceeded = {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val loggedUser = authViewModel.checkBiometricLogin()
                        if (loggedUser != null) onSuccessfulLogin(loggedUser)
                        else authViewModel.backgroundBlockingTaskOnCourse = false
                    }
                }
            )
        }


        /*------------------------------------------------
        |                 User Interface                 |
        ------------------------------------------------*/
        setContent {
            EtziTheme { AuthenticationScreen(
                authenticationViewModel = authViewModel,
                onSuccessfulLogin = ::onSuccessfulLogin,
                biometricSupportChecker = biometricAuthManager::checkBiometricSupport,
                onBiometricAuthRequested = biometricAuthManager::submitBiometricAuthorization
            ) }
        }
    }


    /*************************************************
     **           Login and Sign In Events          **
     *************************************************/

    /**
     * TODO
     *
     * @param user Logged in user's username
     */
    private fun onSuccessfulLogin(user: AuthUser) {
        // Set the last logged user
        authViewModel.updateLastLoggedUsername(user)


        TODO("Falta ver como conectarlo con la actividad principal")
    }


    /**
     * Deletes the current FCM token and creates a new one. Then subscribes the user to the next topics: All and its username's topic
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun subscribeUser() {
        // Get FCM
        val fcm = FirebaseMessaging.getInstance()
        Log.d("FCM", "DCM obtained")

        // Delete previous token
        fcm.deleteToken().addOnSuccessListener {
            // Get a new token and subscribe the user
            Log.d("FCM", "Token deleted")
            fcm.token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                GlobalScope.launch(Dispatchers.IO) {
                    Log.d("FCM", "New Token ${task.result}")
                    httpClient.subscribeUser(task.result)
                    Log.d("FCM", "User subscribed")
                }
            })
        }
    }
}