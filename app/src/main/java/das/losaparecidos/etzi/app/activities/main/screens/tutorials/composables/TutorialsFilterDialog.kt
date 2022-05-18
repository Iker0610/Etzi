package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TransparentDatePicker
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.components.DynamicMediumTopAppBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsFilterDialog(
    tutorialsViewModel: TutorialsViewModel,
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    // TODO: SI NO HAY ASIGNATURAS Y/O PROFES, O EN EL FILTRO NO APARECE NINGUNO, PONER UN MENSAJE EN LA UI SIMILAR A 'NO HAY TUTOORIAS'

    val fechaDesde = rememberSaveable { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE)) }
    val fechaHasta = rememberSaveable { mutableStateOf(LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_DATE)) }

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
                    TextButton(onClick = { /*TODO aplicar lo seleccionado en los filtros*/ }) {
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
            SubjectsMenu(
                asignaturas = tutorialsViewModel.subjectTutorials,
                modifier = Modifier.fillMaxWidth(),
                onSubjectSelected = { /*TODO COGER VALOR DEL COMBOBOX*/ }
            )


            MaterialDivider(Modifier.padding(vertical = 8.dp))


            FilterSectionTitle(icon = Icons.Rounded.DateRange, text = stringResource(id = R.string.subject))

            // TODO OBTENER CORRECTAMENTE LAS FECHAS
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TransparentDatePicker(
                    textFieldValue = fechaDesde,
                    textLabel = stringResource(id = R.string.date_from_label),
                    context = context,
                    onDateRangeSelected = { f1, f2 -> Log.i("fechas", "$f1 $f2") },
                    modifier = Modifier.weight(1f)
                )

                TransparentDatePicker(
                    textFieldValue = fechaHasta,
                    textLabel = stringResource(id = R.string.date_to_label),
                    context = context,
                    modifier = Modifier.weight(1f),
                    onDateRangeSelected = { f1, f2 -> Log.i("fechas", "$f1 $f2") }
                )
            }


            MaterialDivider(Modifier.padding(vertical = 8.dp))


            FilterSectionTitle(icon = Icons.Rounded.School, text = stringResource(id = R.string.professors_label))

            //TODO OBTENER PROFES DE LA BBDD Y MODIFICAR EL onSelectedChanged
            ChipGroup(
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