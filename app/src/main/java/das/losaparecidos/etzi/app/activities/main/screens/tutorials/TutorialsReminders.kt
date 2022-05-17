package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsRemindersScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.TutorialReminders.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ){ paddingValues ->

    }
}