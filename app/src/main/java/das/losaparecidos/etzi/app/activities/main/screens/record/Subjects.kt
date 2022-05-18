package das.losaparecidos.etzi.app.activities.main.screens.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.record.composables.CourseContainer
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {


    val cursos = listOf(1, 2, 3, 4)

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Subjects.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->
        // TODO get datos reales

        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTab) {
                cursos.forEachIndexed { index, course ->
                    Tab(
                        text = { Text("${course}º curso") },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CourseContainer(selectedTab+1)

        }
    }
}

@Preview
@Composable
fun SubjectScreenPreview() {
    EtziTheme {
        SubjectsScreen(WindowWidthSizeClass.Expanded, {})
    }
}

