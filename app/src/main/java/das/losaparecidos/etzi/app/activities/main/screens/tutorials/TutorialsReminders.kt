package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsRemindersScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    Scaffold (
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