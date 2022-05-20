package das.losaparecidos.etzi.app.activities.main.screens.timetable

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EventBusy
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.timetable.composables.LectureCard
import das.losaparecidos.etzi.app.activities.main.viewmodels.StudentDataViewModel
import das.losaparecidos.etzi.app.ui.components.*
import das.losaparecidos.etzi.model.entities.Lecture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(studentDataViewModel: StudentDataViewModel, windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit) {

    val context = LocalContext.current

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember { TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DynamicLargeMediumTopAppBar(
                windowSizeClass,
                title = { Text(text = MainActivityScreens.Timetable.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showDatePicker(context, studentDataViewModel.currentSelectedDay, studentDataViewModel::onSelectedDateChange)
                    }) {
                        Icon(Icons.Rounded.Today, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        when {
            studentDataViewModel.loadingData -> {
                CenteredBox(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 5.dp, modifier = Modifier.size(48.dp))
                }
            }

            studentDataViewModel.timeTable.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    items(studentDataViewModel.timeTable, key = Lecture::hashCode) { lecture ->
                        LectureCard(lecture = lecture)
                    }
                }
            }

            else -> EmptyCollectionScreen(Icons.Rounded.EventBusy, stringResource(R.string.no_lectures_dialog_message), Modifier.padding(paddingValues))

        }
    }
}