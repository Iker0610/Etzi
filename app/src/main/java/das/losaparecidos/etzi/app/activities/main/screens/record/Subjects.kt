package das.losaparecidos.etzi.app.activities.main.screens.record

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.record.composables.CourseContainer
import das.losaparecidos.etzi.app.activities.main.viewmodels.RecordViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredBox
import das.losaparecidos.etzi.app.ui.components.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun SubjectsScreen(
    recordViewModel: RecordViewModel,
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val courses = IntArray(4) { it + 1 }
    val record by recordViewModel.recordGroupedByCourse.collectAsState(initial = emptyMap())

    val pagerState = rememberPagerState()


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
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions -> TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)) }
                    ) {
                        courses.forEachIndexed { index, course ->
                            Tab(
                                text = { Text("${course}º ${stringResource(id = R.string.course)}") },
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalPager(
                        count = courses.size,
                        state = pagerState,

                    ) { pageIndex ->
                        CourseContainer(record[pageIndex + 1] ?: emptyList())
                    }
                }
            }
        }
    }
}

