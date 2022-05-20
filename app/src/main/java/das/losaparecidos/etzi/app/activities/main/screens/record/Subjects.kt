package das.losaparecidos.etzi.app.activities.main.screens.record

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.record.composables.CourseContainer
import das.losaparecidos.etzi.app.activities.main.viewmodels.RecordViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    recordViewModel: RecordViewModel,
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit
) {


    val cursos = 1..4

    var selectedTab by remember { mutableStateOf(0) }
    val record by recordViewModel.recordGroupedByCourse.collectAsState(initial = emptyMap())
    val selectedCourseRecord by derivedStateOf { record[selectedTab + 1] ?: emptyList() }


    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Subjects.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->

        when {
            recordViewModel.loadingData -> {
                CenteredBox(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 5.dp, modifier = Modifier.size(48.dp))
                }
            }

            else -> {
                Column(modifier = Modifier.padding(paddingValues)) {
                    TabRow(selectedTabIndex = selectedTab) {
                        cursos.forEach { course ->
                            Tab(
                                text = { Text("${course}º ${stringResource(id = R.string.course)}") },
                                selected = selectedTab == course - 1,
                                onClick = { selectedTab = course - 1 }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CourseContainer(selectedCourseRecord)
                }
            }
        }
    }
}

