package das.losaparecidos.etzi.app.activities.main.screens.timetable

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import das.losaparecidos.etzi.app.activities.main.screens.MainActivityScreens
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(windowSizeClass: WindowWidthSizeClass) {
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    Scaffold (
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Timetable.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = { scope.launch { navigationDrawerState.open() } }) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ){ paddingValues ->

    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun TimetableScreenPreview() {
    EtziTheme {
        TimetableScreen(WindowWidthSizeClass.Expanded)
    }
}