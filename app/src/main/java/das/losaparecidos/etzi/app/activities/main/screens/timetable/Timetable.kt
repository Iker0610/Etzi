package das.losaparecidos.etzi.app.activities.main.screens.timetable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.timetable.composables.LectureCard
import das.losaparecidos.etzi.app.activities.main.viewmodels.TimetableViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.DynamicLargeTopAppBar
import das.losaparecidos.etzi.app.ui.components.EmptyCollectionScreen
import das.losaparecidos.etzi.app.ui.components.showDatePicker
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.Lecture
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*
import kotlin.math.ln

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(timetableViewModel: TimetableViewModel, windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit, onNavigate: () -> Unit) {

    val context = LocalContext.current

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember { TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec) }

    val currentSelectedDay by timetableViewModel.currentSelectedDay.collectAsState(initial = LocalDate.today)
    val timetable by timetableViewModel.timeTable.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.apply {
            if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact)
                nestedScroll(scrollBehavior.nestedScrollConnection)
        },
        topBar = {
            DynamicLargeTopAppBar(
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
                        showDatePicker(context, currentSelectedDay, timetableViewModel::onSelectedDateChange)
                    }) {
                        Icon(Icons.Rounded.Today, null)
                    }
                    IconButton(onClick = {
                        onNavigate()
                    }) {
                        Icon(MainActivityScreens.Account.icon, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            TimetableNavigationBar(currentSelectedDay, timetableViewModel::onSelectedDateChange)

            Crossfade(targetState = timetable, animationSpec = tween(500), modifier = Modifier.weight(1f)) { timetable ->
                when {
                    timetable.isNotEmpty() -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                        ) {
                            items(timetable, key = Lecture::hashCode) { lecture ->
                                LectureCard(lecture = lecture)
                            }
                        }
                    }

                    else -> EmptyCollectionScreen(Icons.Rounded.EventBusy, stringResource(R.string.no_lectures_dialog_message))
                }
            }
        }
    }
}

@Composable
private fun TimetableNavigationBar(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val context = LocalContext.current

    CenteredRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceTint
                    .copy(alpha = (((4.5f * ln(3.0.dp.value + 1)) + 2f) / 100f))
                    .compositeOver(MaterialTheme.colorScheme.surface)
            )

    ) {
        IconButton(onClick = { onDateChange(date.minus(DatePeriod(days = 7))) }) {
            Icon(Icons.Rounded.FirstPage, null, tint = MaterialTheme.colorScheme.secondary)
        }

        IconButton(onClick = { onDateChange(date.minus(DatePeriod(days = 1))) }) {
            Icon(Icons.Rounded.NavigateBefore, null, tint = MaterialTheme.colorScheme.primary)
        }

        TextButton(onClick = { showDatePicker(context, date, onDateChange) }, modifier = Modifier.weight(1f, fill = false)) {
            Text(
                text = "${date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))} - ${date.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())}",
                style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp)
            )
        }

        IconButton(onClick = { onDateChange(date.plus(DatePeriod(days = 1))) }) {
            Icon(Icons.Rounded.NavigateNext, null, tint = MaterialTheme.colorScheme.primary)
        }

        IconButton(onClick = { onDateChange(date.plus(DatePeriod(days = 7))) }) {
            Icon(Icons.Rounded.LastPage, null, tint = MaterialTheme.colorScheme.secondary)
        }
    }
}