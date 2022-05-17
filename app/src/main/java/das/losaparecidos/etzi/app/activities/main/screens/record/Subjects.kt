package das.losaparecidos.etzi.app.activities.main.screens.record

import android.widget.ExpandableListView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.record.Composable.CurseContainer
import das.losaparecidos.etzi.app.activities.main.screens.record.Composable.SubjectContainer
import das.losaparecidos.etzi.app.activities.main.screens.record.composable.SubjectCard
import das.losaparecidos.etzi.app.activities.main.screens.timetable.TimetableScreen
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.Subject
import das.losaparecidos.etzi.model.entities.SubjectEntity
import das.losaparecidos.etzi.model.mockdata.subjectEntitys
import org.json.JSONObject
import java.lang.reflect.Array
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(windowSizeClass: WindowWidthSizeClass, onMenuOpen: () -> Unit) {
    val cursos = listOf(
        "Curso: Primero",
        "Curso: Segundo",
        "Curso: Tercero",
        "Curso: Cuarto"
    )
    var primerCurso = mutableListOf<SubjectEntity>()
    var segundoCurso = mutableListOf<SubjectEntity>()
    var tercerCurso = mutableListOf<SubjectEntity>()
    var cuartoCurso = mutableListOf<SubjectEntity>()

    subjectEntitys.forEach { subject ->
        when (subject.course) {
            1 -> primerCurso.add(subject)
            2 -> segundoCurso.add(subject)
            3 -> tercerCurso.add(subject)
            4 -> cuartoCurso.add(subject)
        }
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Subjects.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->
        // TODO get datos reales
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues()
        ) {
            item {
                CurseContainer(title = "Asignaturas") {
                    LazyColumn(
                        modifier = Modifier.padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        contentPadding = PaddingValues()
                    ) {
                        cursos.forEach { curso ->
                            item {
                                SubjectContainer(title = curso) {
                                    when (curso) {
                                        "1" -> primerCurso.forEach { subjectEntity ->
                                            SubjectCard(subject = subjectEntity)
                                        }
                                        "2" -> segundoCurso.forEach { subjectEntity ->
                                            SubjectCard(subject = subjectEntity)
                                        }
                                        "3" -> tercerCurso.forEach { subjectEntity ->
                                            SubjectCard(subject = subjectEntity)
                                        }
                                        "4" -> cuartoCurso.forEach { subjectEntity ->
                                            SubjectCard(subject = subjectEntity)
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                Divider()
            }

        }
    }
}

@Preview
@Composable
fun SubjectScreenPreview(){
    EtziTheme {
        SubjectsScreen(WindowWidthSizeClass.Expanded, {})
    }
}

