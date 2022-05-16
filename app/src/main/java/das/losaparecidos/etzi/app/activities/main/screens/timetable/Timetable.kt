package das.losaparecidos.etzi.app.activities.main.screens.timetable

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.timetable.composables.LectureCard
import das.losaparecidos.etzi.app.ui.components.showDatePicker
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import lectures
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {

    val context = LocalContext.current

    // Formato de fecha
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Se inicializa conla fecha actual
    val (selectedDate, setSelectedDate) = rememberSaveable {
        mutableStateOf(
            LocalDate.now()
        )
    }


    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }



    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(text = MainActivityScreens.Timetable.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showDatePicker(context) { date -> setSelectedDate(date) }
                    }) {
                        Icon(Icons.Rounded.Today, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        // TODO GET HORARIO REAL
        LazyColumn(

            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)) {
            lectures.forEach { lecture ->
                if (lecture.startDate.toLocalDate() == selectedDate) {
                    item { LectureCard(lecture = lecture) }
                }
            }
        }


    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun TimetableScreenPreview() {
    EtziTheme {
        TimetableScreen(WindowWidthSizeClass.Expanded, {})
    }
}