package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.model.mockdata.subjects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseContainer(
    selectedCourse: Int,
) {

    val (selectedSubject, setSelectedSubject) = remember { mutableStateOf("") }

    Column {

        subjects.forEach { subject ->

            if (subject.course == selectedCourse) {

                Card(onClick = {

                    // Al clicar cambiar selección
                    if (selectedSubject == subject.name) setSelectedSubject("")
                    else setSelectedSubject(subject.name)

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    // Si la asignatura es del curso


                    // Poner título
                    Text(text = subject.name)

                    // Si está seleccionada
                    if (selectedSubject == subject.name) {

                        // Poner info de la asignatura
                        SubjectContainer(subject = subject)
                    }
                }
            }

        }

    }
}