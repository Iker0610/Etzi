package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.timetable.composables.LectureCard
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.ChipGroup
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.TutorialCard
import das.losaparecidos.etzi.app.ui.components.form.ValidatorTextField
import das.losaparecidos.etzi.app.ui.components.showDatePicker
import das.losaparecidos.etzi.app.ui.components.showDateRangePicker
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.mockdata.tutorials
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {
    val context = LocalContext.current
    // TODO: SI NO HAY ASIGNATURAS Y/O PROFES, O EN EL FILTRO NO APARECE NINGUNO, PONER UN MENSAJE EN LA UI SIMILAR A 'NO HAY TUTOORIAS'
    val asignaturas = listOf("","Desarrollo Avanzado de Software", "Álgebra", "Cálculo", "Desarrollo de Aplicaciones Web Enriquecidas")
    val profes = listOf(
        Professor("Iker", "Sobrón", "iker.sobron@ehu.eus"),
        Professor("Alicia", "Pérez", "alicia.perez@ehu.eus"),
        Professor("Aitziber", "Atutxa", "aitziber.atutxa@ehu.eus"),
        Professor("Koldobika", "Gojenola", "koldo.gojenola@ehu.eus"),
        Professor("Ainhoa", "Yera", "ainhoa.yera@ehu.eus"),
        Professor("Begoña", "blanco", "begona.blanco@ehu.eus"),
        Professor("Iñigo", "Mendialdua", "inigo.mendialdua@ehu.eus"),
        Professor("Mikel", "Villamañe", "mikel.v@ehu.eus"),
        Professor("Pepe", "Perez", "pepe.perez@ehu.eus"),
        Professor("Unprofe", "Más", "unprofe.mas@ehu.eus"),
        Professor("Otroprofe", "Más", "otroprofe.mas@ehu.eus"),
    )
    val fechaDesde = remember{ mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE))}
    val fechaHasta = remember{ mutableStateOf(LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_DATE))}
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Tutorials.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {
            //TODO OBTENER PROFES DE LA BBDD Y MODIFICAR EL onSelectedChanged
            ChipGroup(
                professors = profes
            )
            // TODO OBTENER ASIGNATURAS DE LA BBDD Y QUE SE MANIPULEN BIEN LOS DATOS
            AsignaturasMenu(
                asignaturas = asignaturas,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
            // TODO OBTENER CORRECTAMENTE LAS FECHAS
            TransparentDatePicker(
                textFieldValue = fechaDesde,
                textLabel = stringResource(id = R.string.date_from_label),
                context = context,
                onDateRangeSelected = { f1, f2 -> Log.i("fechas", "$f1 $f2") },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
            TransparentDatePicker(
                textFieldValue = fechaHasta,
                textLabel = stringResource(id = R.string.date_to_label),
                context = context,
                onDateRangeSelected = { f1, f2 -> Log.i("fechas", "$f1 $f2") },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
            tutorials.forEach{ tutorial ->
                TutorialCard(tutorial = tutorial)
            }
        }
    }
}

@Composable
fun TransparentDatePicker(
    modifier: Modifier = Modifier,
    textFieldValue: MutableState<String>,
    textLabel: String,
    trailingIcon: @Composable() (() -> Unit)? = null,
    context: Context,
    onDateRangeSelected: (LocalDate,LocalDate) -> Unit
) {
    ValidatorTextField(
        value = textFieldValue.value,
        label = {Text(textLabel)},
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignaturasMenu(asignaturas: List<String>, modifier: Modifier = Modifier) {
    val default = 0

    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(asignaturas[default]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
            .width(150.dp)
            .fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = selectedType,
            onValueChange = { /*TODO ASER KOSITAS CUANDO SE SELECCIONE 1*/ },
            label = { Text(stringResource(id = R.string.subject)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            asignaturas.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedType = selectionOption
                        expanded = false
                    }, text = {Text(text = selectionOption)}
                )
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
@Preview(showBackground = true,uiMode = Configuration.UI_MODE_NIGHT_YES)
fun TutorialsScreenPreview() {
    EtziTheme {
        TutorialsScreen(windowSizeClass = WindowWidthSizeClass.Expanded, {})
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun TransparentDatePickerPreview() {
    val context = LocalContext.current
    EtziTheme {
        TransparentDatePicker(
            textFieldValue = mutableStateOf(LocalDate.now().toString()),
            textLabel = "Prueba" ,
            context = context,
            onDateRangeSelected = {f1, f2 -> Log.i("fechas" , "$f1 $f2")}
        )
    }
}

