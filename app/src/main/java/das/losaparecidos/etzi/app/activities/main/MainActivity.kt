package das.losaparecidos.etzi.app.activities.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.app.activities.main.screens.account.AccountScreen
import das.losaparecidos.etzi.app.activities.main.screens.egela.EgelaScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.CreditsScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.GradesScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.SubjectsScreen
import das.losaparecidos.etzi.app.activities.main.screens.timetable.TimetableScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsRemindersScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsScreen
import das.losaparecidos.etzi.app.activities.main.viewmodels.UserDataViewModel
import das.losaparecidos.etzi.app.ui.components.EtziNavigationBar
import das.losaparecidos.etzi.app.ui.components.EtziNavigationDrawer
import das.losaparecidos.etzi.app.ui.components.EtziNavigationRail
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
    val currentNavBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
    val currentRoute by derivedStateOf { currentNavBackStackEntry?.destination?.route }
    val currentSection by derivedStateOf { MainActivityScreens.screenRouteToSectionRouteMapping[currentRoute] }


    //-----------   Navigation States   ------------//
    val enableNavigationElements by derivedStateOf { MainActivityScreens.hasNavigationElements(currentRoute) }
    val enableBottomNavigation by derivedStateOf { enableNavigationElements && windowSizeClass == WindowWidthSizeClass.Compact }
    val enableNavigationRail by derivedStateOf { enableNavigationElements && windowSizeClass != WindowWidthSizeClass.Compact }


    //-----------   Navigation-drawer   ------------//
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    /*************************************************
     **                    Events                   **
     *************************************************/

    // Close bottom navigation menu if it was open and we changed layout to the one with a navigation rail
    LaunchedEffect(navigationDrawerState) {
        if (!enableBottomNavigation && navigationDrawerState.currentValue != DrawerValue.Closed) {
            scope.launch { navigationDrawerState.close() }
        }
    }



    /*************************************************
     **            Common Event Callbacks           **
     *************************************************/

    // Navigate to a route
    val onNavigate = { route: String ->
        navController.navigate(route) {
            popUpTo(MainActivityScreens.Timetable.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Open menu
    val onNavigationMenuOpen: () -> Unit = { scope.launch { navigationDrawerState.open() } }


    /*************************************************
     **                   Main UI                   **
     *************************************************/

    /*
     ENVOLVER EL NAVIGATION GRAPH CON LOS ELEMENTOS DE NAVEGACIÓN:

     - Primero con el navigation drawer
     - Luego con el navigation rail SI FUERA NECESARIO
     - Luego añadir el bottom navigation bar SI FUERA NECESARIO

      */

    EtziNavigationDrawer(currentRoute, onNavigate, navigationDrawerState, enableNavigationElements) {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(enableBottomNavigation,
                    enter = slideInVertically(initialOffsetY = { it }) + expandVertically(),
                    exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
                ) { EtziNavigationBar(currentSection, onNavigate) }
            }
        ) { paddingValues ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedVisibility(
                    enableNavigationRail,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + expandHorizontally(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + shrinkHorizontally()
                ) { EtziNavigationRail(currentSection, onNavigate, onNavigationMenuOpen) }

                MainNavigationGraph(userDataViewModel, navController, windowSizeClass, onNavigationMenuOpen)
            }
        }
    }


    //------------   Debug Log Screen   ------------//
    LaunchedEffect(currentRoute) {
        Log.d("navigation",
            "Back stack changed $currentRoute \n ${
                navController.backQueue.joinToString(
                    "  -  "
                ) { it.destination.route ?: "root_path" }
            }"
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainNavigationGraph(
    userDataViewModel: UserDataViewModel,
    navController: NavHostController,
    windowSizeClass: WindowWidthSizeClass,
    onNavigationMenuOpen: () -> Unit
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
        startDestination = MainActivityScreens.Timetable.route
    ) {
        composable(
            route = MainActivityScreens.Timetable.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            TimetableScreen(windowSizeClass, onNavigationMenuOpen)
        }

        navigation(
            route = MainActivityScreens.TutorialsSection.route,
            startDestination = MainActivityScreens.Tutorials.route
        ) {
            composable(route = MainActivityScreens.Tutorials.route) {
                TutorialsScreen(windowSizeClass, onNavigationMenuOpen)
            }

            composable(route = MainActivityScreens.TutorialReminders.route) {
                TutorialsRemindersScreen(windowSizeClass, onNavigationMenuOpen)
            }
        }

        navigation(
            route = MainActivityScreens.Record.route,
            startDestination = MainActivityScreens.Grades.route
        ) {
            composable(route = MainActivityScreens.Grades.route) {
                GradesScreen(windowSizeClass, onNavigationMenuOpen)
            }

            composable(route = MainActivityScreens.Subjects.route) {
                SubjectsScreen(windowSizeClass, onNavigationMenuOpen)
            }

            composable(route = MainActivityScreens.Credits.route) {
                CreditsScreen(windowSizeClass, onNavigationMenuOpen)
            }
        }

        composable(route = MainActivityScreens.Egela.route) {
            EgelaScreen(windowSizeClass, onNavigationMenuOpen)
        }

        composable(route = MainActivityScreens.Account.route) {
            AccountScreen()
        }
    }
}
