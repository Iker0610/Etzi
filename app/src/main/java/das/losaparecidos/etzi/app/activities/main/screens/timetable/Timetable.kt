package das.losaparecidos.etzi.app.activities.main.screens.timetable

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EventBusy
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import das.losaparecidos.etzi.app.ui.components.CenteredBox
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.DynamicLargeMediumTopAppBar
import das.losaparecidos.etzi.app.ui.components.showDatePicker
import das.losaparecidos.etzi.model.entities.Lecture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(studentDataViewModel: StudentDataViewModel, windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit) {

    val context = LocalContext.current

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }


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
            studentDataViewModel.loadingData ->{
                CenteredBox(Modifier
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

            else -> {
                CenteredBox(Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp)
                ) {
                    Card {
                        BoxWithConstraints {
                            if (400.dp <= maxHeight) {
                                CenteredColumn(Modifier.padding(horizontal = 32.dp, vertical = 64.dp)) {
                                    Icon(
                                        Icons.Rounded.EventBusy,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(32.dp))
                                    Text(text = stringResource(R.string.no_lectures_dialog_message), style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
                                }
                            } else {
                                CenteredRow(Modifier.padding(horizontal = 64.dp, vertical = 32.dp)) {
                                    Icon(
                                        Icons.Rounded.EventBusy,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.width(32.dp))
                                    Text(text = stringResource(R.string.no_lectures_dialog_message), style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}