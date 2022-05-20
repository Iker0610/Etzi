@file:OptIn(ExperimentalMaterial3Api::class)

package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EventBusy
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables.TutorialCard
import das.losaparecidos.etzi.app.activities.main.viewmodels.TutorialsViewModel
import das.losaparecidos.etzi.app.ui.components.*
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
    var currentExpandedSubject: String? by rememberSaveable { mutableStateOf(null) }
    var currentExpandedProfessor: String? by rememberSaveable { mutableStateOf(null) }


    // TODO: SI NO HAY ASIGNATURAS Y/O PROFES, O EN EL FILTRO NO APARECE NINGUNO, PONER UN MENSAJE EN LA UI SIMILAR A 'NO HAY TUTORIAS'
    Scaffold(
        topBar = {
            DynamicLargeMediumTopAppBar(
                windowSizeClass = windowSizeClass,
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

        when {
            tutorialsViewModel.loadingData -> {
                CenteredBox(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 5.dp, modifier = Modifier.size(48.dp))
                }
            }

            tutorials.isEmpty() -> {
                EmptyCollectionScreen(Icons.Rounded.EventBusy, stringResource(R.string.no_tutorials_dialog_message), Modifier.padding(paddingValues))
            }

            else -> {
                val singleSubjectSelection by derivedStateOf { tutorials.size == 1 }
                SideEffect { if (singleSubjectSelection) currentExpandedProfessor = tutorials.first().subjectName }
                CenteredColumn(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    //header por asignatura y divisores de material design 3
                    //surface para diferenciar las asignaturas

                    tutorials.forEachIndexed { index, subjectWithTutorial ->
                        SubjectCollapsableSection(
                            subjectWithTutorial.subjectName,
                            collapsible = !singleSubjectSelection,
                            expanded = subjectWithTutorial.subjectName == currentExpandedSubject,
                            secondaryExpanded = currentExpandedProfessor != null,
                            onExpand = {
                                currentExpandedSubject = if (currentExpandedSubject != subjectWithTutorial.subjectName) subjectWithTutorial.subjectName else null
                                currentExpandedProfessor = null
                            }
                        ) {
                            val singleProfessorSelection by derivedStateOf { subjectWithTutorial.professors.size == 1 }
                            LaunchedEffect(singleProfessorSelection) { if (singleProfessorSelection) currentExpandedProfessor = subjectWithTutorial.professors.first().email }

                            subjectWithTutorial.professors.forEach { professor ->
                                ProfessorCollapsable(
                                    professor = professor.professor,
                                    collapsible = !singleProfessorSelection,
                                    expanded = currentExpandedProfessor == professor.email,
                                    onExpand = { currentExpandedProfessor = if (currentExpandedProfessor != professor.email) professor.email else null }
                                ) {
                                    professor.tutorials.forEach { tutorial ->
                                        TutorialCard(tutorial = tutorial, professor = professor.professor)
                                    }
                                }
                            }
                        }

                        if (index + 1 != tutorials.size) {
                            MaterialDivider()
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
    collapsible: Boolean = true,
    expanded: Boolean = true,
    secondaryExpanded: Boolean = false,
    onExpand: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val expanded = expanded || (!collapsible)
    val backgroundColor by animateColorAsState(
        targetValue = when {
            expanded && secondaryExpanded -> MaterialTheme.colorScheme.secondary
            expanded -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 500)
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            expanded && secondaryExpanded -> MaterialTheme.colorScheme.onSecondary
            expanded -> MaterialTheme.colorScheme.onPrimaryContainer
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(durationMillis = 500)
    )

    Column(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExpand,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            CenteredRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(text = subjectName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                if (collapsible) {
                    IconButton(
                        onClick = onExpand,
                        modifier = Modifier.padding(start = 32.dp),
                        enabled = collapsible
                    ) {
                        Icon(Icons.Rounded.ExpandMore, contentDescription = "Expand more for detail", Modifier.size(32.dp))
                    }
                }
            }
        }

        AnimatedVisibility(expanded) {
            Column {
                content()
            }
        }
    }
}


@Composable
fun ProfessorCollapsable(
    professor: Professor,
    modifier: Modifier = Modifier,
    collapsible: Boolean = true,
    expanded: Boolean = true,
    onExpand: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val expanded = expanded || !collapsible

    val backgroundColor by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 500)
    )

    Column(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExpand,
            color = backgroundColor
        ) {
            CenteredRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = professor.fullName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium), modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.padding(start = 24.dp),
                    onClick = onExpand,
                    enabled = collapsible
                ) {
                    val color = if (collapsible) LocalContentColor.current else Color.Transparent
                    Icon(Icons.Rounded.ExpandMore, contentDescription = "Expand more for detail", tint = color)
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
