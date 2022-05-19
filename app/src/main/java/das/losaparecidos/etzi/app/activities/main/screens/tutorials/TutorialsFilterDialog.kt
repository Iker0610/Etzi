package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.FilterChipGroup
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.SubjectDropdownMenu
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsFilterViewModel
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.DynamicMediumTopAppBar
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.components.form.DateRangeDoubleField
import das.losaparecidos.etzi.model.entities.ProfessorWithTutorials

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
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit
) {


    // EVENTS
    val onSave = {
        tutorialsViewModel.onFilterChange(
            tutorialsFilterViewModel.currentSelectedSubject.let { if (it != "-") it else null },
            tutorialsFilterViewModel.startDate,
            tutorialsFilterViewModel.endDate,
            tutorialsFilterViewModel.selectedProfessors,
        )
    }

    // UI

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicMediumTopAppBar(
                title = { Text(text = MainActivityScreens.Tutorials.title(LocalContext.current)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Rounded.Close, null) }
                },
                actions = {
                    TextButton(onClick = onSave) { Text(text = stringResource(id = R.string.save_button)) }
                },
                windowSizeClass = windowSizeClass
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
            FilterSectionTitle(icon = Icons.Rounded.MenuBook, text = stringResource(id = R.string.subject))


            SubjectDropdownMenu(
                subjectList = tutorialsViewModel.subjectList,
                selectedSubject = tutorialsFilterViewModel.currentSelectedSubject,
                modifier = Modifier.fillMaxWidth(),
                onSubjectSelected = tutorialsFilterViewModel::currentSelectedSubject::set

            )


            MaterialDivider(Modifier.padding(vertical = 8.dp))


            FilterSectionTitle(icon = Icons.Rounded.DateRange, text = stringResource(id = R.string.date_range))


            DateRangeDoubleField(
                dateRange = tutorialsFilterViewModel.dateRange,
                onDateRangeSelected = tutorialsFilterViewModel::onDateRangeChange,
            )

            MaterialDivider(Modifier.padding(vertical = 8.dp))


            FilterSectionTitle(icon = Icons.Rounded.School, text = stringResource(id = R.string.professors_label))


            FilterChipGroup(
                items = tutorialsFilterViewModel.selectedProfessors,
                onItemToggle = tutorialsFilterViewModel::onProfessorToggle,
                itemToStringMapper = ProfessorWithTutorials::fullName
            )
        }
    }
}

@Composable
private fun FilterSectionTitle(text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}