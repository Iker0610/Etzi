package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import das.losaparecidos.etzi.app.activities.main.MainActivity
import das.losaparecidos.etzi.app.activities.main.screens.MainActivityScreens
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(windowSizeClass: WindowWidthSizeClass) {
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Tutorials.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = { scope.launch { navigationDrawerState.open() } }) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->

    }
}

@Composable
@Preview
fun TutorialsScreenPreview() {
    EtziTheme() {
        TutorialsScreen(windowSizeClass = WindowWidthSizeClass.Expanded)
    }
}

