package das.losaparecidos.etzi.app.activities.main.screens.record

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(text = MainActivityScreens.Grades.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->

    }
}