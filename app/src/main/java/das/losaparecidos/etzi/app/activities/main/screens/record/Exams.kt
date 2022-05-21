package das.losaparecidos.etzi.app.activities.main.screens.record

import QuarterSubjectsList
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPasteOff
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.record.composables.CourseSubjectsList
import das.losaparecidos.etzi.app.activities.main.viewmodels.RecordViewModel
import das.losaparecidos.etzi.app.ui.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun ExamsScreen(
    recordViewModel: RecordViewModel,
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val courseRecord by recordViewModel.actualCourseRecord.collectAsState(initial = emptyList())

    val quarters = IntArray(2) { it + 1 }
    val pagerState = rememberPagerState()

    Scaffold(
        topBar = {
            DynamicLargeMediumTopAppBar(
                windowSizeClass = windowSizeClass,
                title = { Text(text = MainActivityScreens.Exams.title(LocalContext.current)) },
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

            courseRecord.isNotEmpty() -> {

                Column(modifier = Modifier.padding(paddingValues)) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions -> TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)) }
                    ) {
                        quarters.forEachIndexed { index, course ->
                            Tab(
                                text = { Text("${course}º ${stringResource(id = R.string.quarter)}") },
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                            )
                        }
                    }

                    HorizontalPager(
                        count = quarters.size,
                        state = pagerState,

                        ) { pageIndex ->

                        if (pageIndex == 1) {
                            QuarterSubjectsList(subjects = courseRecord.filter {
                                it.subjectCalls[0].examDate.monthNumber in 5..7
                            })
                        }

                        else {
                            QuarterSubjectsList(subjects = courseRecord.filter {
                                it.subjectCalls[0].examDate.monthNumber > 11 && it.subjectCalls[0].examDate.monthNumber < 2
                            })
                        }
                    }
                }
            }
            else -> EmptyCollectionScreen(Icons.Rounded.ContentPasteOff, stringResource(id = R.string.no_exams), Modifier.padding(paddingValues))
        }
    }
}