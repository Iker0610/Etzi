package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Filter
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.ChipGroup
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.SubjectsMenu
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.TutorialCard
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.form.ValidatorTextField
import das.losaparecidos.etzi.app.ui.components.showDateRangePicker
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalDate as JLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit,
    tutorialsViewModel: TutorialsViewModel,
    onFilter: () -> Unit = {}
) {
    val context = LocalContext.current
    // TODO: SI NO HAY ASIGNATURAS Y/O PROFES, O EN EL FILTRO NO APARECE NINGUNO, PONER UN MENSAJE EN LA UI SIMILAR A 'NO HAY TUTOORIAS'

    val fechaDesde = rememberSaveable { mutableStateOf(
        java.time.LocalDate.now().format(
            DateTimeFormatter.ISO_DATE)) }
    val fechaHasta = rememberSaveable { mutableStateOf(
        java.time.LocalDate.now().plusDays(7).format(
            DateTimeFormatter.ISO_DATE)) }

    Scaffold(
            topBar = {
                SmallTopAppBar(
                        title = { Text(text = MainActivityScreens.Tutorials.title(LocalContext.current)) },
                        navigationIcon = {
                            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                                IconButton(onClick = onMenuOpen) {
                                    Icon(Icons.Rounded.Menu, null)
                                }
                            }
                        },
                        actions = { IconButton(onClick = onFilter) {
                            Icon(Icons.Rounded.FilterAlt, contentDescription = null)
                        }})
            }
    ) { paddingValues ->
        //TODO SI NO HAY TUTORIAS CON LOS PARAMETROS SELECCIONADOS, PONER UN MENSAJE SIMILAR A 'No hay tutorias disponibles'
        CenteredColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {
            LazyColumn {
                items(tutorialsViewModel.tutorials) { subjectutorial ->
                    subjectutorial.professors.map{ professorWithTutorials ->
                        professorWithTutorials.tutorials.map{ tutorial ->
                            TutorialCard(tutorial = tutorial, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransparentDatePicker(
        modifier: Modifier = Modifier,
        textFieldValue: MutableState<String>,
        textLabel: String,
        context: Context,
        onDateRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    ValidatorTextField(
            value = textFieldValue.value,
            label = { Text(textLabel) },
            trailingIcon = {
                IconButton(
                        onClick = {
                            showDateRangePicker(context, onDateRangeSelected)
                        }
                ) {
                    Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Toggle Date Icon"
                    )
                }
            },
            keyboardActions = KeyboardActions.Default,
            modifier = modifier,
            onValueChange = {}
    )
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun TutorialsScreenPreview() {
    EtziTheme {
        TutorialsScreen(windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(300.dp, 300.dp)), {}, hiltViewModel())
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun TransparentDatePickerPreview() {
    val context = LocalContext.current
    EtziTheme {
        TransparentDatePicker(
                textFieldValue = mutableStateOf(JLocalDate.now().toString()),
                textLabel = "Prueba",
                context = context,
                onDateRangeSelected = { f1, f2 -> Log.i("fechas", "$f1 $f2") }
        )
    }
}

