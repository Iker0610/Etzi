package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import android.util.Log
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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.components.DynamicMediumTopAppBar
import das.losaparecidos.etzi.app.ui.components.form.DateRangeDoubleField
import das.losaparecidos.etzi.app.utils.today
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsFilterDialog(
    tutorialsViewModel: TutorialsViewModel,
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var fromDate by remember { mutableStateOf(LocalDate.today) }
    var toDate by remember { mutableStateOf(LocalDate.today.plus(7,DateTimeUnit.DAY)) }
    val selectedSubject = rememberSaveable{ mutableStateOf(tutorialsViewModel.subjectTutorials.first())}

    Scaffold(
        topBar = {
            DynamicMediumTopAppBar(
                title = { Text(text = MainActivityScreens.Tutorials.title(LocalContext.current)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.Close, null)
                    }
                },
                actions = {
                    // TODO poner text button 'save'
                    TextButton(onClick = {
                        /*TODO aplicar lo seleccionado en los filtros*/
                        tutorialsViewModel.onSelectedChange(selectedSubject.value, fromDate, toDate, emptyList())
                    }) {
                        Text(text = stringResource(id = R.string.save_button))
                    }
                },
                windowSizeClass = windowSizeClass
            )
        },
        modifier = Modifier.fillMaxSize()
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

            // TODO OBTENER ASIGNATURAS DE LA BBDD Y QUE SE MANIPULEN BIEN LOS DATOS
            SubjectDropdownMenu(
                asignaturas = tutorialsViewModel.subjectTutorials,
                modifier = Modifier.fillMaxWidth(),
                onSubjectSelected = { subject -> selectedSubject.value = subject }
            )


            MaterialDivider(Modifier.padding(vertical = 8.dp))


            FilterSectionTitle(icon = Icons.Rounded.DateRange, text = stringResource(id = R.string.date_range))

            // TODO OBTENER CORRECTAMENTE LAS FECHAS

            DateRangeDoubleField(
                dateRange = Pair(fromDate, toDate),
                onDateRangeSelected = { f1, f2 ->
                    Log.i("fechas", "$f1 $f2")
                    fromDate = f1
                    toDate = f2
                },
            )

            MaterialDivider(Modifier.padding(vertical = 8.dp))


            FilterSectionTitle(icon = Icons.Rounded.School, text = stringResource(id = R.string.professors_label))

            //TODO OBTENER PROFES DE LA BBDD Y MODIFICAR EL onSelectedChanged
            FilterChipGroup(
                professors = tutorialsViewModel.professorsWithTutorials,
                onSelectedChanged = { p1, p2 -> {} },
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