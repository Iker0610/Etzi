package das.losaparecidos.etzi.app.activities.main.screens.record

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPasteOff
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import subjectEnrollments

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit) {

    val (selectedSubject, setSelectedSubject) = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Grades.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->

        CenteredColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {

            var hayNotas = false

            subjectEnrollments.forEach { subjectEnrollment ->

                // Si existe la convocatoria actual, está evaluada y la nota es provisional
                if (subjectEnrollment.subjectCalls.isNotEmpty()
                    && subjectEnrollment.subjectCalls.last().subjectCallAttendances.isNotEmpty()
                    && subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].provisional
                ) {

                    Card(
                        onClick = {

                            // Al clicar cambiar selección
                            if (selectedSubject == subjectEnrollment.subject.name) {
                                setSelectedSubject("")
                            }
                            else setSelectedSubject(subjectEnrollment.subject.name)

                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {


                        // Poner título
                        Text(text = subjectEnrollment.subject.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp))
                        hayNotas = true

                        // Si está seleccionada
                        if (selectedSubject == subjectEnrollment.subject.name) {

                            // Datos de la asignatura
                            Column(modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(bottom = 16.dp)) {

                                // Curso
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {


                                        Text(
                                            "${stringResource(id = R.string.course).capitalize()}:",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                        Text(
                                            text = "${subjectEnrollment.subject.course}º ${
                                                stringResource(
                                                    id = R.string.course
                                                )
                                            }",
                                            style = MaterialTheme.typography.labelMedium,
                                        )

                                }

                                // Convocatoria
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {


                                        Text(
                                            text = "${stringResource(id = R.string.call)}:",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                        Text(
                                            text = subjectEnrollment.subjectCalls.last().callType,
                                            style = MaterialTheme.typography.labelMedium
                                        )

                                }

                                //Nota
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                                    Text(
                                        text = "${stringResource(id = R.string.grade)}:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(text = subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade,
                                        style = MaterialTheme.typography.labelLarge,)
                                    if (subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].distinction) {
                                        Text(text = "(${stringResource(id = R.string.distinction)})",
                                            style = MaterialTheme.typography.labelLarge)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!hayNotas) {
                Spacer(modifier = Modifier.height(32.dp))
                Icon(
                    Icons.Rounded.ContentPasteOff,
                    null,
                    modifier = Modifier.size(128.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    stringResource(id = R.string.noGrades),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }
}