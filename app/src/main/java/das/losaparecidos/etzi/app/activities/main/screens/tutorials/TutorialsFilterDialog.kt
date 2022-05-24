package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.FilterChipGroup
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.SubjectDropdownMenu
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsFilterViewModel
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.components.form.DateRangeDoubleField
import das.losaparecidos.etzi.app.ui.components.form.SectionTitle
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.ProfessorWithTutorials
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsFilterDialog(
    tutorialsViewModel: TutorialsViewModel,
    tutorialsFilterViewModel: TutorialsFilterViewModel = TutorialsFilterViewModel(
        tutorialsViewModel.selectedSubject,
        tutorialsViewModel.startDate,
        tutorialsViewModel.endDate,
        tutorialsViewModel.selectedProfessors
    ),
    onClose: () -> Unit
) {

    val selectableSubjectList by derivedStateOf { listOf("-", *tutorialsViewModel.subjectList.toTypedArray()) }

    // EVENTS
    val onSave = {
        tutorialsViewModel.onFilterChange(
            tutorialsFilterViewModel.currentSelectedSubject.let { if (it != "-") it else null },
            tutorialsFilterViewModel.startDate,
            tutorialsFilterViewModel.endDate,
            tutorialsFilterViewModel.selectedProfessors,
        )

        onClose()
    }

    // UI
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.surface

    SideEffect {
        systemUiController.setSystemBarsColor(backgroundColor, darkIcons = useDarkIcons)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Tutorials.title(LocalContext.current)) },
                navigationIcon = {
                    IconButton(onClick = onClose) { Icon(Icons.Rounded.Close, null) }
                },
                actions = {
                    TextButton(onClick = onSave) { Text(text = stringResource(id = R.string.save_button)) }
                },
                // windowSizeClass = windowSizeClass
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(icon = Icons.Rounded.Book, text = stringResource(id = R.string.subject))


            SubjectDropdownMenu(
                subjectList = selectableSubjectList,
                selectedSubject = tutorialsFilterViewModel.currentSelectedSubject,
                modifier = Modifier.fillMaxWidth(),
                onSubjectSelected = tutorialsFilterViewModel::currentSelectedSubject::set

            )


            MaterialDivider(Modifier.padding(vertical = 8.dp))


            SectionTitle(icon = Icons.Rounded.DateRange, text = stringResource(id = R.string.date_range))


            DateRangeDoubleField(
                dateRange = tutorialsFilterViewModel.dateRange,
                onDateRangeSelected = tutorialsFilterViewModel::onDateRangeChange,

                startDateTrailingIcon = {
                    IconButton(onClick = { tutorialsFilterViewModel.startDate = LocalDate.today }, modifier = Modifier.size(20.dp)) {
                        Icon(Icons.Rounded.RestartAlt , null, tint = MaterialTheme.colorScheme.secondary)
                    }
                },

                endDateTrailingIcon = {
                    IconButton(onClick = { tutorialsFilterViewModel.endDate = null }, modifier = Modifier.size(20.dp)) {
                        Icon(Icons.Rounded.HighlightOff , null, tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )

            MaterialDivider(Modifier.padding(vertical = 8.dp))


            SectionTitle(icon = Icons.Rounded.School, text = stringResource(id = R.string.professors_label))


            FilterChipGroup(
                items = tutorialsFilterViewModel.selectedProfessors,
                onItemToggle = tutorialsFilterViewModel::onProfessorToggle,
                itemToStringMapper = ProfessorWithTutorials::fullName
            )
        }
    }
}
