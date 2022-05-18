package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Filter
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.TransparentDatePicker
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.DynamicMediumTopAppBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsFilterDialog(
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit,
    tutorialsViewModel: TutorialsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    // TODO: SI NO HAY ASIGNATURAS Y/O PROFES, O EN EL FILTRO NO APARECE NINGUNO, PONER UN MENSAJE EN LA UI SIMILAR A 'NO HAY TUTOORIAS'

    val fechaDesde = rememberSaveable { mutableStateOf(
        LocalDate.now().format(
            DateTimeFormatter.ISO_DATE)) }
    val fechaHasta = rememberSaveable { mutableStateOf(
        LocalDate.now().plusDays(7).format(
            DateTimeFormatter.ISO_DATE)) }

    Scaffold(
        topBar = {
            DynamicMediumTopAppBar(
                title = { Text(text = MainActivityScreens.Tutorials.title(LocalContext.current)) },
                navigationIcon = {
                    IconButton(onClick = onMenuOpen) {
                        Icon(Icons.Rounded.Close, null)
                    }
                },
                actions = { IconButton(onClick = onBack) {
                    // TODO poner text button 'save'
                    Icon(Icons.Rounded.Save, contentDescription = null)
                }
                }, windowSizeClass = windowSizeClass)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        CenteredColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {

            // TODO OBTENER ASIGNATURAS DE LA BBDD Y QUE SE MANIPULEN BIEN LOS DATOS
            SubjectsMenu(
                asignaturas = tutorialsViewModel.subjectTutorials,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                onSubjectSelected = { /*TODO COGER VALOR DEL COMBOBOX*/ }
            )
            // TODO OBTENER CORRECTAMENTE LAS FECHAS
            Row {
                TransparentDatePicker(
                    textFieldValue = fechaDesde,
                    textLabel = stringResource(id = R.string.date_from_label),
                    context = context,
                    onDateRangeSelected = { f1, f2 -> Log.i("fechas", "$f1 $f2") },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
                TransparentDatePicker(
                    textFieldValue = fechaHasta,
                    textLabel = stringResource(id = R.string.date_to_label),
                    context = context,
                    onDateRangeSelected = { f1, f2 ->
                        Log.i("fechas", "$f1 $f2")
                        //fechaDesde.value = f1.toString()
                        //fechaHasta.value = f2.toString()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .fillMaxWidth()
                )
            }
            //TODO OBTENER PROFES DE LA BBDD Y MODIFICAR EL onSelectedChanged
            ChipGroup(
                professors = tutorialsViewModel.professorsWithTutorials,
                onSelectedChanged = { p1, p2 -> {} },
            )
        }
    }
}