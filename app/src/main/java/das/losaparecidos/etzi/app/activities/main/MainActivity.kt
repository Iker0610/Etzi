package das.losaparecidos.etzi.app.activities.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.app.activities.main.screens.AnimatedSplashScreen
import das.losaparecidos.etzi.app.activities.main.screens.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.account.AccountScreen
import das.losaparecidos.etzi.app.activities.main.screens.egela.EgelaScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.CreditsScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.GradesScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.SubjectsScreen
import das.losaparecidos.etzi.app.activities.main.screens.timetable.TimetableScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsRemindersScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsScreen
import das.losaparecidos.etzi.app.activities.main.viewmodels.UserDataViewModel
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                EtziAppScreen(userDataViewModel, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EtziAppScreen(
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

    /*************************************************
     **            Common Event Callbacks           **
     *************************************************/

    // Navigate to a route
    val onNavigate = { route: String ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }


    /*************************************************
     **                   Main UI                   **
     *************************************************/

    /*
     TODO ENVOLVER EL NAVIGATION GRAPH CON LOS ELEMENTOS DE NAVEGACIÓN:

     - Primero con el navigation drawer
     - Luego con el navigation rail SI FUERA NECESARIO
     - Luego añadir el bottom navigation bar SI FUERA NECESARIO

      * Recordatorio: para el navigation drawer hay que crear aquí el drawerState y pasarlo a las cosas que lo necesiten
      */
    Scaffold(
        topBar = { TODO() },
        bottomBar = { TODO() }
    ) { paddingValues ->
        MainNavigationGraph(userDataViewModel, navController, windowSizeClass, paddingValues)
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainNavigationGraph(
    userDataViewModel: UserDataViewModel,
    navController: NavHostController,
    windowSizeClass: WindowWidthSizeClass,
    paddingValues: PaddingValues,
) {
    /*************************************************
     **             Variables and States            **
     *************************************************/

    //-----------   Utility variables   ------------//
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    /*************************************************
     **            Common Event Callbacks           **
     *************************************************/

    // Pop current screen from backstack (if we didn't pop anything make sure to travel to main screen)
    val navigateBack: () -> Unit = {
        scope.launch(Dispatchers.Main) {
            if (!navController.popBackStack()) {
                navController.navigate(MainActivityScreens.Timetable.route)
            }
        }
    }

    // Navigate to the current user's account page. (Passing the current user as a parameter in the route)
    val onNavigateToAccount = { navController.navigate(MainActivityScreens.Account.route) { launchSingleTop = true } }


    /*************************************************
     **                   Main UI                   **
     *************************************************/

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
            TimetableScreen(windowSizeClass)
        }

        navigation(
            route = MainActivityScreens.TutorialsSection.route,
            startDestination = MainActivityScreens.Tutorials.route
        ) {
            composable(route = MainActivityScreens.Tutorials.route) {
                TutorialsScreen(windowSizeClass)
            }

            composable(route = MainActivityScreens.TutorialReminders.route) {
                TutorialsRemindersScreen(windowSizeClass)
            }
        }

        navigation(
            route = MainActivityScreens.Record.route,
            startDestination = MainActivityScreens.Grades.route
        ) {
            composable(route = MainActivityScreens.Grades.route) {
                GradesScreen(windowSizeClass)
            }

            composable(route = MainActivityScreens.Subjects.route) {
                SubjectsScreen(windowSizeClass)
            }

            composable(route = MainActivityScreens.Credits.route) {
                CreditsScreen(windowSizeClass)
            }
        }

        composable(route = MainActivityScreens.Egela.route) {
            EgelaScreen(windowSizeClass)
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