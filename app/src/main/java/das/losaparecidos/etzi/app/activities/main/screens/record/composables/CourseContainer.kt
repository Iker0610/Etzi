package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Expand
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.model.entities.SubjectEnrollment
import das.losaparecidos.etzi.model.mockdata.subjects
import subjectEnrollments

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseContainer(subjects: List<SubjectEnrollment>) {

    val (selectedSubject, setSelectedSubject) = remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        subjects.forEach { subjectEnrollment ->
            Card(
                onClick = {
                    // Al clicar cambiar selección
                    if (selectedSubject == subjectEnrollment.subject.name) setSelectedSubject("")
                    else setSelectedSubject(subjectEnrollment.subject.name)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                {
                    // Poner título
                    Text(
                        text = subjectEnrollment.subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(0.9f)
                    )

                    if (subjectEnrollment.subject.name == selectedSubject){
                        Icon(Icons.Rounded.ExpandLess, null,
                            modifier = Modifier.weight(0.1f))
                    } else {
                        Icon(Icons.Rounded.ExpandMore, null,
                            modifier = Modifier.weight(0.1f))
                    }


                }

                // Si está seleccionada
                if (selectedSubject == subjectEnrollment.subject.name) {
                    // Poner info de la asignatura
                    SubjectContainer(subjectEnrollment = subjectEnrollment)
                }
            }

        }

    }
}