package das.losaparecidos.etzi.app.activities.main.screens.record

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import das.losaparecidos.etzi.app.activities.main.viewmodels.RecordViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.model.entities.SubjectEnrollment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(
    recordViewModel: RecordViewModel,
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit
) {

    val (selectedSubject, setSelectedSubject) = remember { mutableStateOf("") }

    val subjectEnrollments = recordViewModel.obtainProvisionalSubjectGrades()

    val onExpand = { subjectEnrollment: SubjectEnrollment ->

        // Al clicar cambiar selección
        if (selectedSubject == subjectEnrollment.subject.name) {
            setSelectedSubject("")
        } else setSelectedSubject(subjectEnrollment.subject.name)

    }

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

            if (subjectEnrollments.isNotEmpty()) {
                subjectEnrollments.forEach { subjectEnrollment ->
                    Card(
                        onClick = { onExpand(subjectEnrollment) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {

                        CenteredRow(horizontalArrangement = Arrangement.SpaceBetween) {
                            // Poner título
                            Text(
                                text = subjectEnrollment.subject.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .weight(1f)
                            )

                            // Curso
                            Surface(
                                modifier = Modifier.padding(16.dp, 8.dp),
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = MaterialTheme.shapes.small
                            ) {
                                CenteredRow(
                                    modifier = Modifier.padding(
                                        vertical = 4.dp,
                                        horizontal = 8.dp
                                    )
                                ) {
                                    Text(
                                        text = "${subjectEnrollment.subject.course}º ${stringResource(id = R.string.course)}",
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                }
                            }
                        }
                        // Si está seleccionada
                        if (selectedSubject == subjectEnrollment.subject.name) {

                            // Datos de la asignatura
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {

                                // Convocatoria
                                Column(
                                ) {
                                    Text(
                                        text = "${stringResource(id = R.string.call)}:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = subjectEnrollment.subjectCalls.last().callType,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }

                                //Nota
                                Column(
                                ) {
                                    Text(
                                        text = "${stringResource(id = R.string.grade)}:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade,
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                    if (subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].distinction) {
                                        Text(
                                            text = "(${stringResource(id = R.string.distinction)})",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
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