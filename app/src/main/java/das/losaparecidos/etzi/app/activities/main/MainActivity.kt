package das.losaparecidos.etzi.app.activities.main

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.screens.AnimatedSplashScreen
import das.losaparecidos.etzi.app.activities.main.screens.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.timetable.TimetableScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsRemindersScreen
import das.losaparecidos.etzi.app.activities.main.screens.account.AccountScreen
import das.losaparecidos.etzi.app.activities.main.screens.egela.EgelaScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.CreditsScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.GradesScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.SubjectsScreen
import das.losaparecidos.etzi.app.activities.main.viewmodels.UserDataViewModel
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /*************************************************
     **                  ViewModel                  **
     *************************************************/

    private val userDataViewModel: UserDataViewModel by viewModels()


    /*************************************************
     **          Activity Lifecycle Methods         **
     *************************************************/

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EtziTheme {
                val navController: NavHostController = rememberAnimatedNavController()
                MainNavigationGraph(userDataViewModel, navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalAnimationApi::class)
@Composable
private fun MainNavigationGraph(
    userDataViewModel: UserDataViewModel,
    navController: NavHostController,
) {
    /*************************************************
     **             Variables and States            **
     *************************************************/

    //-----------   Utility variables   ------------//
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val windowSizeClass = calculateWindowSizeClass(context as Activity).widthSizeClass


    //-------------   Nav-Controller   -------------//

    val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

    AnimatedNavHost(
        navController = navController,
        startDestination = MainActivityScreens.Splash.route
    ) {


        composable(
            route = MainActivityScreens.Splash.route,
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            AnimatedSplashScreen {
                navController.popBackStack() // Empty the backstack so the user doesn't return to splash screen
                navController.navigate("home")
            }
        }

        composable(
            route = MainActivityScreens.Timetable.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            TimetableScreen()
        }

        navigation(
            route = MainActivityScreens.TutorialsSection.route,
            startDestination = MainActivityScreens.Tutorials.route
        ) {
            composable(route = MainActivityScreens.Tutorials.route) {
                TutorialsScreen()
            }

            composable(route = MainActivityScreens.TutorialReminders.route) {
                TutorialsRemindersScreen()
            }
        }

        navigation(
            route = MainActivityScreens.Record.route,
            startDestination = MainActivityScreens.Grades.route
        ) {
            composable(route = MainActivityScreens.Grades.route) {
                GradesScreen()
            }

            composable(route = MainActivityScreens.Subjects.route) {
                SubjectsScreen()
            }

            composable(route = MainActivityScreens.Credits.route) {
                CreditsScreen()
            }
        }

        composable(route = MainActivityScreens.Egela.route) {
            EgelaScreen()
        }

        composable(route = MainActivityScreens.Account.route) {
            AccountScreen()
        }


        // TODO: Borrar cuando tengamos pantallas
        /*composable(
            route = "home",
            enterTransition = { fadeIn(animationSpec = tween(500)) }
        ) {
            Home()
        }*/
    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Inicio", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "TODO: Perfil de usuario", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Rounded.AccountCircle, null)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 32.dp, horizontal = 16.dp)
        ) {
            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ehu_logo),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp))
                    Text(
                        text = "Etzi",
                        style = MaterialTheme.typography.displayLarge,
                    )
                }
            }
        }
    }
}*/