package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
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

                // Poner título
                Text(
                    text = subjectEnrollment.subject.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                // Si está seleccionada
                if (selectedSubject == subjectEnrollment.subject.name) {
                    // Poner info de la asignatura
                    SubjectContainer(subjectEnrollment = subjectEnrollment)
                }
            }

        }

    }
}