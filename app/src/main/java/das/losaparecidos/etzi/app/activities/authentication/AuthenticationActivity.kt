package das.losaparecidos.etzi.app.activities.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.WidgetOpenerActions
import das.losaparecidos.etzi.app.activities.authentication.screens.AnimatedSplashScreen
import das.losaparecidos.etzi.app.activities.authentication.screens.AuthenticationScreen
import das.losaparecidos.etzi.app.activities.main.MainActivity
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.BiometricAuthManager
import das.losaparecidos.etzi.model.entities.AuthUser
import das.losaparecidos.etzi.model.webclients.APIClient
import das.losaparecidos.etzi.services.StudentDataUpdateWorker
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : FragmentActivity() {

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

    @OptIn(ExperimentalAnimationApi::class)
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
            EtziTheme {
                val scope = rememberCoroutineScope()

                // Initialize Navigation (used for transition between splash screen and auth screen)
                val navController = rememberAnimatedNavController()

                val preLoginStarted by rememberSaveable { mutableStateOf(true) }
                var splashScreenFinished by rememberSaveable { mutableStateOf(false) }
                var preLoginFinished by rememberSaveable { mutableStateOf(false) }
                var preLoginSuccessful by rememberSaveable { mutableStateOf(false) }

                AnimatedNavHost(
                    navController = navController,
                    startDestination = "splash_screen"
                ) {
                    //-------------   Splash Screen   --------------//
                    composable(
                        route = "splash_screen",
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
                        AnimatedSplashScreen { splashScreenFinished = true }
                    }

                    //----------   Authorization Screen   ----------//
                    composable(
                        route = "auth_screen",
                        enterTransition = { fadeIn(animationSpec = tween(500)) }
                    ) {
                        AuthenticationScreen(
                            authenticationViewModel = authViewModel,
                            onSuccessfulLogin = ::onSuccessfulLogin,
                            biometricSupportChecker = biometricAuthManager::checkBiometricSupport,
                            onBiometricAuthRequested = biometricAuthManager::submitBiometricAuthorization
                        )
                    }
                }

                LaunchedEffect(preLoginStarted) {
                    withContext(Dispatchers.IO) {
                        if (authViewModel.checkRemindLogin() != null) launch(Dispatchers.Main) { preLoginSuccessful = true }
                        launch(Dispatchers.Main) { preLoginFinished = true }
                    }
                }

                LaunchedEffect(preLoginFinished, splashScreenFinished) {
                    if (preLoginFinished && splashScreenFinished) {
                        if (preLoginSuccessful) onSuccessfulLogin(authViewModel.lastLoggedUser!!)
                        else {
                            launch(Dispatchers.Main) {
                                authViewModel.backgroundBlockingTaskOnCourse = false
                                navController.popBackStack() // Empty the backstack so the user doesn't return to splash screen
                                navController.navigate("auth_screen")
                            }
                        }
                    }
                }
            }
        }
        Log.d("AuthenticationActivity",+intent.getStringExtra("WIDGET_ACTION"))
    }


    /*************************************************
     **           Login and Sign In Events          **
     *************************************************/

    /**
     * It updates the last logged user on the Datastore and launches the Main Activity
     *
     * @param user Logged in user's username
     */
    private fun onSuccessfulLogin(user: AuthUser) {
        // Load and cache this user data
        loadAndCacheStudentData()

        // Set the last logged user
        authViewModel.updateLastLoggedUsername(user)

        // Subscribe user
        subscribeUser()

        // Update Widgets
//        val updateIntent = Intent(this, VisitsWidgetReceiver::class.java).apply { action = UPDATE_ACTION }
//        this.sendBroadcast(updateIntent)

        // Open the main activity
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("LOGGED_USER_LDAP", user.ldap)
        }

        startActivity(intent)
        finish()
    }

    private fun loadAndCacheStudentData() {
        val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<StudentDataUpdateWorker>().build()
        WorkManager
            .getInstance(this)
            .enqueue(uploadWorkRequest)

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