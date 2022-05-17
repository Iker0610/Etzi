package das.losaparecidos.etzi.app.activities.main.screens.record

import YearCreditsScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.model.mockdata.subjects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {

    var selectedTab by remember { mutableStateOf(0) }

    var courses = mutableSetOf(1)
    subjects.forEach{ subject ->
        if (subject.course > 1) courses.add(subject.course)
    }
    courses = courses.toSortedSet()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Credits.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ){  paddingValues ->

        Column (modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTab) {
                courses.forEachIndexed { index, course ->
                    Tab(
                        text = { Text("${course}º ${stringResource(id = R.string.course)}") },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CenteredColumn(modifier = Modifier.fillMaxSize()) {

                YearCreditsScreen(selectedTab)
            }
        }

    }
}