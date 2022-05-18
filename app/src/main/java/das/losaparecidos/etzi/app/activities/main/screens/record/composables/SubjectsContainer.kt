package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import das.losaparecidos.etzi.model.entities.Subject

enum class EstadoAsignaturas { VISIBLE, HIDDEN }

@Composable
fun SubjectContainer(
    subject: Subject
) {

    Row() {
        Column() {
            Text(text = "Type:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(text = subject.type)
        }
        Column() {
            Text(text = "Date:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(text = subject.academicYear.toString())
        }
        Column() {
            Text(text = "Grade:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(text = "10")
        }
    }


}