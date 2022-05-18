package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.model.mockdata.subjects
import subjectEnrollments

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseContainer(
    selectedCourse: Int,
) {

    val (selectedSubject, setSelectedSubject) = remember { mutableStateOf("") }

    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {

        subjectEnrollments.forEach { subjectEnrollment ->

            if (subjectEnrollment.subject.course == selectedCourse) {

                Card(
                    onClick = {

                        // Al clicar cambiar selección
                        if (selectedSubject == subjectEnrollment.subject.name) setSelectedSubject("")
                        else setSelectedSubject(subjectEnrollment.subject.name)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    ) {
                    // Si la asignatura es del curso


                    // Poner título
                    Text(text = subjectEnrollment.subject.name)

                    // Si está seleccionada
                    if (selectedSubject == subjectEnrollment.subject.name) {

                        // Poner info de la asignatura
                        SubjectContainer(subjectEnrollment = subjectEnrollment)
                    }
                }
            }

        }

    }
}