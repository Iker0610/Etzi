package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.model.entities.Subject
import das.losaparecidos.etzi.model.entities.SubjectEnrollment

@Composable
fun SubjectContainer(
    subjectEnrollment: SubjectEnrollment
) {

    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.type)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(text = subjectEnrollment.subject.type)
        }
        Column(
            Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.date)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(text = subjectEnrollment.subject.academicYear.toString())
        }
        Column(
            Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.grade)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )

            // Si existe la convocatoria actual, está evaluada y la nota NO es provisional
            if (subjectEnrollment.subjectCalls.isNotEmpty()
                && subjectEnrollment.subjectCalls.last().subjectCallAttendances.isNotEmpty()
                && !subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].provisional
            ) {

                Row() {
                    Text(text = subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade)

                    // Si tiene matrícula de honor
                    if (subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].distinction) {
                        Text(text = " ${stringResource(id = R.string.distinction)}")
                    }
                }

            }
        }
    }
}