package das.losaparecidos.etzi.app.activities.main.screens.timetable

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.account.AccountIcon
import das.losaparecidos.etzi.app.activities.main.screens.timetable.composables.LectureCard
import das.losaparecidos.etzi.app.activities.main.viewmodels.AccountViewModel
import das.losaparecidos.etzi.app.activities.main.viewmodels.TimetableViewModel
import das.losaparecidos.etzi.app.ui.components.*
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.entities.LectureReminder
import das.losaparecidos.etzi.services.ReminderManager
import das.losaparecidos.etzi.services.ReminderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
fun TimetableScreen(timetableViewModel: TimetableViewModel, windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit, onNavigate: () -> Unit, accountViewModel: AccountViewModel) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val currentSelectedDay by timetableViewModel.currentSelectedDay.collectAsState(initial = LocalDate.today)
    val timetable by timetableViewModel.timeTable.collectAsState(initial = emptyList())
    val lectureReminderStatuses by timetableViewModel.lectureRemainders.collectAsState(initial = emptyMap())

    var loadingData by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        scope.launch {
            delay(350)
            loadingData = false
        }
    }

    // Events
    // Set or unset item alarm
    val onLectureRemainder: (Lecture, ReminderStatus) -> Unit = { lecture, remainderStatus ->
        val baseLectureReminder = LectureReminder(lecture = lecture)

        when (remainderStatus) {
            ReminderStatus.UNAVAILABLE -> {
                Log.d("REMAINDERS", "onItemRemainder when status is UNAVAILABLE")
            }

            ReminderStatus.OFF -> {
                scope.launch(Dispatchers.IO) {
                    timetableViewModel.addLectureReminder(baseLectureReminder)?.let {
                        ReminderManager.addLectureReminder(context, it)

                        scope.launch(Dispatchers.Main) {
                            Toast.makeText(context, context.resources.getString(R.string.lecture_reminder_set_toast), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            ReminderStatus.ON -> {
                scope.launch(Dispatchers.IO) {
                    timetableViewModel.removeLectureReminder(baseLectureReminder)?.let {
                        ReminderManager.removeLectureReminder(context, it)
                    }
                }
                Toast.makeText(context, "Remainder removed.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //-----------------------------------------------------------------------------------------

    // Exit dialog
    /*************************************************
     **                Event Handlers               **
     *************************************************/

    var showExitAlertDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler { showExitAlertDialog = true }


    /*------------------------------------------------
    |                    Dialogs                     |
    ------------------------------------------------*/
    if (showExitAlertDialog) {
        AlertDialog(
            icon = { Icon(Icons.Rounded.Error, null) },
            title = { Text(stringResource(R.string.app_exit_dialog_title), textAlign = TextAlign.Center) },
            text = { Text(stringResource(R.string.app_exit_dialog_text)) },
            confirmButton = {
                TextButton(onClick = { (context as Activity).finish() }) {
                    Text(text = stringResource(id = R.string.exit_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitAlertDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel_button))
                }
            },
            onDismissRequest = { showExitAlertDialog = false }
        )
    }

    //-----------------------------------------------------------------------------------------

    /*************************************************
     **                User Interface               **
     *************************************************/

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
                        showDatePicker(context, currentSelectedDay, timetableViewModel::onSelectedDateChange)
                    }) {
                        Icon(Icons.Rounded.Today, null)
                    }
                    AccountIcon(accountViewModel, onNavigate)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            TimetableNavigationBar(currentSelectedDay, timetableViewModel::onSelectedDateChange)

            Crossfade(targetState = loadingData, animationSpec = tween(500), modifier = Modifier.weight(1f)) { showLoader ->
                if (showLoader) {

                    CenteredBox(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(32.dp)
                    ) {
                        CircularProgressIndicator(strokeWidth = 5.dp, modifier = Modifier.size(48.dp))
                    }
                } else {
                    Crossfade(targetState = timetable, animationSpec = tween(500), modifier = Modifier.weight(1f)) { timetable ->
                        when {
                            timetable.isNotEmpty() -> {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                                ) {
                                    items(timetable, key = Lecture::hashCode) { lecture ->
                                        LectureCard(lecture = lecture, lectureReminderStatuses[lecture] ?: ReminderStatus.UNAVAILABLE, onLectureRemainder)
                                    }
                                }
                            }

                            else -> EmptyCollectionScreen(Icons.Rounded.EventBusy, stringResource(R.string.no_lectures_dialog_message))
                        }
                    }
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