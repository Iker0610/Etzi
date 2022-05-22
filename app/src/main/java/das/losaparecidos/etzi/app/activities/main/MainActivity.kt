package das.losaparecidos.etzi.app.activities.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.app.activities.main.screens.account.AccountScreen
import das.losaparecidos.etzi.app.activities.main.screens.egela.EgelaScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.CreditsScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.ExamsScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.GradesScreen
import das.losaparecidos.etzi.app.activities.main.screens.record.SubjectsScreen
import das.losaparecidos.etzi.app.activities.main.screens.timetable.TimetableScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsFilterDialog
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsRemindersScreen
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TutorialsScreen
import das.losaparecidos.etzi.app.activities.main.viewmodels.*
import das.losaparecidos.etzi.app.ui.components.EtziNavigationBar
import das.losaparecidos.etzi.app.ui.components.EtziNavigationDrawer
import das.losaparecidos.etzi.app.ui.components.EtziNavigationRail
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /*************************************************
     **                  ViewModel                  **
     *************************************************/

    private val timetableViewModel: TimetableViewModel by viewModels()
    private val accountViewModel: AccountViewModel by viewModels()

    /*************************************************
     **          Activity Lifecycle Methods         **
     *************************************************/

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EtziTheme {
                accountViewModel.reloadLang(accountViewModel.prefLang.collectAsState(initial = accountViewModel.currentSetLang).value, this)
                val navController: NavHostController = rememberAnimatedNavController()
                EtziAppScreen(timetableViewModel, navController, accountViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EtziAppScreen(
    timetableViewModel: TimetableViewModel,
    navController: NavHostController,
    accountViewModel: AccountViewModel
) {
    /*************************************************
     **             Variables and States            **
     *************************************************/


    //-----------   Utility variables   ------------//
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val windowSizeClass = calculateWindowSizeClass(context as Activity)


    //-------------   Nav-Controller   -------------//
    val currentNavBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
    val currentRoute by derivedStateOf { currentNavBackStackEntry?.destination?.route }
    val currentSection by derivedStateOf { MainActivityScreens.screenRouteToSectionRouteMapping[currentRoute] }


    //-----------   Navigation States   ------------//
    val enableNavigationElements by derivedStateOf {
        MainActivityScreens.hasNavigationElements(
            currentRoute
        )
    }
    val enableBottomNavigation by derivedStateOf { enableNavigationElements && windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact }
    val enableNavigationRail by derivedStateOf { enableNavigationElements && windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact }


    //-----------   Navigation-drawer   ------------//
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val enableNavigationDrawerGestures by derivedStateOf { enableNavigationElements && navigationDrawerState.isOpen }


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
            popUpTo(route) {
                inclusive = true
            }
            launchSingleTop = true
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

    EtziNavigationDrawer(
        currentRoute = currentRoute,
        onNavigate = onNavigate,
        drawerState = navigationDrawerState,
        gesturesEnabled = enableNavigationDrawerGestures,
    ) {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    enableBottomNavigation,
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

                MainNavigationGraph(
                    timetableViewModel,
                    navController,
                    windowSizeClass,
                    onNavigationMenuOpen,
                    accountViewModel
                )
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


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun MainNavigationGraph(
    timetableViewModel: TimetableViewModel,
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    onNavigationMenuOpen: () -> Unit,
    accountViewModel: AccountViewModel,
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
            TimetableScreen(timetableViewModel, windowSizeClass, onNavigationMenuOpen, onNavigateToAccount, accountViewModel)
        }

        navigation(
            route = MainActivityScreens.TutorialsSection.route,
            startDestination = MainActivityScreens.Tutorials.route
        ) {
            composable(route = MainActivityScreens.Tutorials.route) {
                val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.TutorialsSection.route) }
                val tutorialsViewModel: TutorialsViewModel = hiltViewModel(recordBackStackEntry)

                TutorialsScreen(tutorialsViewModel, windowSizeClass, onNavigationMenuOpen, { navController.navigate("dialog_filter") }, accountViewModel, onNavigateToAccount)
            }

            composable(route = MainActivityScreens.TutorialReminders.route) {
                val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.TutorialsSection.route) }
                // val tutorialsViewModel: TutorialsViewModel = hiltViewModel(recordBackStackEntry)

                TutorialsRemindersScreen(windowSizeClass, onNavigationMenuOpen)
            }

            dialog(route = "dialog_filter", dialogProperties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false)) {
                val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.TutorialsSection.route) }
                val tutorialsViewModel: TutorialsViewModel = hiltViewModel(recordBackStackEntry)

                TutorialsFilterDialog(
                    tutorialsViewModel = tutorialsViewModel,
                    onClose = navigateBack
                )
            }
        }

        navigation(
            route = MainActivityScreens.Record.route,
            startDestination = MainActivityScreens.Subjects.route
        ) {
            composable(route = MainActivityScreens.Grades.route) {
                val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.Record.route) }
                val recordViewModel: RecordViewModel = hiltViewModel(recordBackStackEntry)

                GradesScreen(recordViewModel, windowSizeClass, onNavigationMenuOpen, accountViewModel, onNavigateToAccount)
            }

            composable(route = MainActivityScreens.Subjects.route) {
                val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.Record.route) }
                val recordViewModel: RecordViewModel = hiltViewModel(recordBackStackEntry)

                SubjectsScreen(recordViewModel, windowSizeClass, onNavigationMenuOpen, onNavigateToAccount, accountViewModel)
            }

            composable(route = MainActivityScreens.Credits.route) {
                val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.Record.route) }
                val recordViewModel: RecordViewModel = hiltViewModel(recordBackStackEntry)

                CreditsScreen(recordViewModel, windowSizeClass, onNavigationMenuOpen, onNavigateToAccount, accountViewModel)
            }

            composable(route = MainActivityScreens.Exams.route) {
                val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.Record.route) }
                val recordViewModel: RecordViewModel = hiltViewModel(recordBackStackEntry)

                ExamsScreen(recordViewModel, windowSizeClass, onNavigationMenuOpen, onNavigateToAccount, accountViewModel)
            }
        }

        composable(route = MainActivityScreens.Egela.route) {
            val egelaBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.Egela.route) }
            val egelaViewModel: EgelaViewModel  = hiltViewModel(egelaBackStackEntry)
            EgelaScreen(windowSizeClass, onNavigationMenuOpen, egelaViewModel, onNavigateToAccount, accountViewModel)
        }

        composable(route = MainActivityScreens.Account.route) {
            //val recordBackStackEntry = remember { navController.getBackStackEntry(MainActivityScreens.Timetable.route) }
            AccountScreen(accountViewModel, windowSizeClass, navigateBack)
        }
    }
}
