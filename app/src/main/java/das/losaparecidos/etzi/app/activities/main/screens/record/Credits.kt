package das.losaparecidos.etzi.app.activities.main.screens.record

import YearCreditsScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import das.losaparecidos.etzi.app.activities.main.viewmodels.RecordViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredBox
import das.losaparecidos.etzi.app.ui.components.DynamicMediumTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsScreen(recordViewModel: RecordViewModel, windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit) {

    var selectedTab by remember { mutableStateOf(0) }

    val courses by recordViewModel.enrolledCourseSet.collectAsState(initial = emptySet())

    val record by recordViewModel.recordGroupedByCourse.collectAsState(initial = emptyMap())
    val selectedCourseRecord by derivedStateOf { record[selectedTab + 1] ?: emptyList() }
    val approvedCreditsInDegree by recordViewModel.approvedCreditsInDegree.collectAsState(initial = 0)


    Scaffold(
        topBar = {
            DynamicMediumTopAppBar(
                windowSizeClass,
                title = { Text(text = MainActivityScreens.Credits.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            recordViewModel.loadingData || courses.isEmpty() -> {
                CenteredBox(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 5.dp, modifier = Modifier.size(48.dp))
                }
            }

            else -> Column(modifier = Modifier.padding(paddingValues)) {
                TabRow(selectedTabIndex = selectedTab) {
                    courses.forEachIndexed { index, course ->
                        Tab(
                            text = { Text("${course}º ${stringResource(id = R.string.course)}") },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
                YearCreditsScreen(selectedCourseRecord, approvedCreditsInDegree)
            }
        }
    }
}