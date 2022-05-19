@file:OptIn(ExperimentalMaterial3Api::class)

package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.TutorialCard
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.Professor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(
    tutorialsViewModel: TutorialsViewModel,
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit,
    onFilter: () -> Unit = {},
) {
    val context = LocalContext.current

    // STATES
    val tutorials by tutorialsViewModel.filteredTutorials.collectAsState(initial = emptyList())


    // TODO: SI NO HAY ASIGNATURAS Y/O PROFES, O EN EL FILTRO NO APARECE NINGUNO, PONER UN MENSAJE EN LA UI SIMILAR A 'NO HAY TUTORIAS'
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Tutorials.title(context)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onFilter) {
                        Icon(Icons.Rounded.FilterAlt, contentDescription = "Open filter dialog")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (tutorials.isEmpty()) {
            //TODO PONER UN MENSAJE SIMILAR A 'No hay tutorias disponibles'
        } else {
            CenteredColumn(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                //header por asignatura y divisores de material design 3
                //surface para diferenciar las asignaturas

                tutorials.forEach { subjectWithTutorial ->
                    SubjectCollapsableSection(subjectWithTutorial.subjectName) {
                        subjectWithTutorial.professors.forEach { professor ->
                            ProfessorCollapsable(professor = professor.professor) {
                                professor.tutorials.forEach { tutorial ->
                                    TutorialCard(tutorial = tutorial, professor = professor.professor)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun SubjectCollapsableSection(
    subjectName: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }
        ) {
            CenteredRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(text = subjectName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Rounded.ExpandMore, contentDescription = "Expand more for detail", modifier = Modifier.padding(start = 32.dp))
                }
            }
        }

        AnimatedVisibility(expanded) {
            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ProfessorCollapsable(
    professor: Professor,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }
        ) {
            CenteredRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(text = professor.fullName, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Rounded.ExpandMore, contentDescription = "Expand more for detail", modifier = Modifier.padding(start = 32.dp))
                }
            }
        }

        AnimatedVisibility(expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                content()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun TutorialsScreenPreview() {
    EtziTheme {
        TutorialsScreen(viewModel(), windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(300.dp, 300.dp)), {})
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SubjectCollapsableSectionPreview() {
    EtziTheme {
        SubjectCollapsableSection("Asignatura")
    }
}


