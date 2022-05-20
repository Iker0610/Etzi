@file:OptIn(ExperimentalMaterial3Api::class)

package das.losaparecidos.etzi.app.activities.main.screens.tutorials

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import das.losaparecidos.etzi.model.entities.ProfessorWithTutorials
import das.losaparecidos.etzi.model.entities.SubjectTutorial

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    var (currentExpandedProfessor, setCurrentExpandedProfessor) = rememberSaveable { mutableStateOf<String?>(null) }

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember { TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                //windowSizeClass = windowSizeClass,
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
                },
                scrollBehavior = scrollBehavior
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
                SideEffect { if (singleSubjectSelection) currentExpandedSubject = tutorials.first().subjectName }

                LazyColumn(modifier = Modifier.padding(paddingValues)) {

                    tutorials.forEachIndexed { index, subjectWithTutorial ->

                        subjectCollapsableSection(
                            subjectWithTutorial = subjectWithTutorial,
                            collapsible = !singleSubjectSelection,
                            expanded = subjectWithTutorial.subjectName == currentExpandedSubject,
                            secondaryExpanded = currentExpandedProfessor != null,
                            onExpand = {
                                currentExpandedSubject = if (currentExpandedSubject != subjectWithTutorial.subjectName) subjectWithTutorial.subjectName else null
                                currentExpandedProfessor = null
                            },
                            currentExpandedProfessor = currentExpandedProfessor,
                            onChangeCurrentExpandedProfessor = setCurrentExpandedProfessor
                        )


                        stickyHeader(key = "${subjectWithTutorial.subjectName}&&content_divider") {
                            if (index + 1 != tutorials.size) {
                                MaterialDivider(Modifier.padding(vertical = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.subjectCollapsableSection(
    subjectWithTutorial: SubjectTutorial,
    collapsible: Boolean = true,
    expanded: Boolean = true,
    secondaryExpanded: Boolean = false,
    onExpand: () -> Unit,
    currentExpandedProfessor: String?,
    onChangeCurrentExpandedProfessor: (String?) -> Unit
) {
    val expand = expanded || (!collapsible)
    val singleProfessorSelection by derivedStateOf { subjectWithTutorial.professors.size == 1 }

    stickyHeader(key = subjectWithTutorial.subjectName) {
        val backgroundColor by animateColorAsState(
            targetValue = when {
                expand && (secondaryExpanded || singleProfessorSelection) -> MaterialTheme.colorScheme.secondary
                expand -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            },
            animationSpec = tween(durationMillis = 500)
        )

        val contentColor by animateColorAsState(
            targetValue = when {
                expand && (secondaryExpanded || singleProfessorSelection) -> MaterialTheme.colorScheme.onSecondary
                expand -> MaterialTheme.colorScheme.onPrimaryContainer
                else -> MaterialTheme.colorScheme.onSurface
            },
            animationSpec = tween(durationMillis = 500)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = { if (collapsible) onExpand() },
            color = backgroundColor,
            contentColor = contentColor
        ) {
            CenteredRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(text = subjectWithTutorial.subjectName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))

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
    }

    if (expand) {

        subjectWithTutorial.professors.forEach { professor ->
            professorCollapsable(
                parentSubject = subjectWithTutorial.subjectName,
                professor = professor,
                collapsible = !singleProfessorSelection,
                expanded = currentExpandedProfessor == professor.email,
                onExpand = {
                    onChangeCurrentExpandedProfessor(
                        if (currentExpandedProfessor != professor.email) professor.email else null
                    )
                }
            )
        }
    }
}


fun LazyListScope.professorCollapsable(
    parentSubject: String,
    professor: ProfessorWithTutorials,
    collapsible: Boolean = true,
    expanded: Boolean = true,
    onExpand: () -> Unit = {}
) {
    val expand = expanded || !collapsible

    item(key = "$parentSubject&&${professor.email}") {

        val backgroundColor by animateColorAsState(
            targetValue = if (expand) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            animationSpec = tween(durationMillis = 500)
        )
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
    }

    item(key = "$parentSubject&&${professor.email}&&tutorialsSpacer") {
        AnimatedVisibility(
            visible = expand,
            enter = fadeIn(animationSpec = TweenSpec(200, 200, FastOutLinearInEasing)) + expandVertically(),
            exit = fadeOut(animationSpec = TweenSpec(200, 0, FastOutLinearInEasing)) + shrinkVertically()
        ) {
            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )
        }
    }

    items(
        items = professor.tutorials,
        key = { "$parentSubject&&${professor.email}&&${it.startDate}" }
    ) { tutorial ->
        AnimatedVisibility(
            visible = expand,
            enter = fadeIn(animationSpec = TweenSpec(200, 200, FastOutLinearInEasing)) + expandVertically(),
            exit = fadeOut(animationSpec = TweenSpec(100, 0, FastOutLinearInEasing)) + shrinkVertically(animationSpec = TweenSpec(200, 200, FastOutLinearInEasing))
        ) {
            TutorialCard(
                tutorial = tutorial, professor = professor.professor,
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
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
