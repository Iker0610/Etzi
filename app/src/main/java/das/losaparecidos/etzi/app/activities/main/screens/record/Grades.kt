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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.account.AccountIcon
import das.losaparecidos.etzi.app.activities.main.viewmodels.AccountViewModel
import das.losaparecidos.etzi.app.activities.main.viewmodels.RecordViewModel
import das.losaparecidos.etzi.app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(
    recordViewModel: RecordViewModel,
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit,
    accountViewModel: AccountViewModel,
    onNavigate: () -> Unit
) {

    val subjectEnrollments by recordViewModel.provisionalSubjectGrades.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            DynamicMediumTopAppBar(
                windowSizeClass = windowSizeClass,
                title = { Text(text = MainActivityScreens.Grades.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                },
                actions = {
                    AccountIcon(accountViewModel, onNavigate)
                }
            )
        }
    ) { paddingValues ->

        when {
            recordViewModel.loadingData -> {
                CenteredBox(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 5.dp, modifier = Modifier.size(48.dp))
                }
            }

            subjectEnrollments.isNotEmpty() -> {
                CenteredColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                ) {
                    subjectEnrollments.forEach { subjectEnrollment ->

                        val grade = subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
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

                                // Nota (número)
                                Surface(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = if (grade.toFloat() >= 5f) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    CenteredRow(
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp
                                        )
                                    ) {
                                        Text(
                                            text = grade,
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                }
                            }

                            // Datos de la asignatura
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {

                                // Curso
                                CenteredColumn{
                                    Text(
                                        text = "${stringResource(id = R.string.course).capitalize(Locale.current)}:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = "${subjectEnrollment.subject.course}º",
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }

                                // Convocatoria
                                Column {
                                    Text(
                                        text = "${stringResource(id = R.string.call)}:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    var call = stringResource(id = R.string.ordinary_text)
                                    if (subjectEnrollment.subjectCalls.last().callType == "Extraordinaria") {
                                        call = stringResource(id = R.string.extraordinary_text)
                                    }
                                    Text(
                                        text = call ,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }

                                // Nota
                                Column {
                                    Text(
                                        text = "${stringResource(id = R.string.grade)}:",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )

                                    if (subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].distinction) {
                                        Text(
                                            text = stringResource(id = R.string.distinction),
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    } else if (grade.toFloat() < 5f) {
                                        Text(
                                            text = stringResource(id = R.string.fail),
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    } else if (grade.toFloat() >= 9f) {
                                        Text(
                                            text = stringResource(id = R.string.outstanding),
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    } else if (grade.toFloat() >= 7f) {
                                        Text(
                                            text = stringResource(id = R.string.notable),
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    } else {
                                        Text(
                                            text = stringResource(id = R.string.pass),
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            else -> EmptyCollectionScreen(Icons.Rounded.ContentPasteOff, stringResource(id = R.string.noGrades), Modifier.padding(paddingValues))
        }
    }
}
