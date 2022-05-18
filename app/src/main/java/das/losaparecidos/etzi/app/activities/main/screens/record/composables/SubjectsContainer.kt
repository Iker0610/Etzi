package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import das.losaparecidos.etzi.model.entities.Subject
import das.losaparecidos.etzi.model.entities.SubjectEnrollment

@Composable
fun SubjectContainer(
    subjectEnrollment: SubjectEnrollment
) {

    Row() {
        Column() {
            Text(text = "Type:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(text = subjectEnrollment.subject.type)
        }
        Column() {
            Text(text = "Date:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(text = subjectEnrollment.subject.academicYear.toString())
        }
        Column() {
            Text(text = "Grade:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)

            // Si existe la convocatoria actual, está evaluada y la nota NO es provisional
            if (subjectEnrollment.subjectCalls.isNotEmpty()
                && subjectEnrollment.subjectCalls.last().subjectCallAttendances.isNotEmpty()
                && !subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].provisional) {

                Row() {
                    Text(text = subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade)

                    // Si tiene matrícula de honor
                    if (subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].distinction){
                        Text(text = "(Matrícula de Honor)")
                    }
                }

            }
        }
    }
}