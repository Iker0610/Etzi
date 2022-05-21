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

    val grade = subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade


    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row() {
            Text(
                text = "${stringResource(id = R.string.type)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(0.2f)
            )
            Spacer(modifier = Modifier.weight(0.05f))

            Text(text = subjectEnrollment.subject.type,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(0.7f))
        }
        Row() {
            Text(
                text = "${stringResource(id = R.string.date)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(0.2f)
            )
            Spacer(modifier = Modifier.weight(0.05f))

            Text(text = subjectEnrollment.subject.academicYear.toString(),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(0.7f))
        }
        Row() {
            Text(
                text = "${stringResource(id = R.string.grade)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(0.2f)
            )
            Spacer(modifier = Modifier.weight(0.05f))

            // Si existe la convocatoria actual, está evaluada y la nota NO es provisional
            if (subjectEnrollment.subjectCalls.isNotEmpty()
                && subjectEnrollment.subjectCalls.last().subjectCallAttendances.isNotEmpty()
                && !subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].provisional
            ) {

                Row(modifier = Modifier.weight(0.7f)) {

                    // Si tiene matrícula de honor
                    if (subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].distinction) {
                        Text(
                            text = stringResource(id = R.string.distinction),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    else if (grade.toFloat() < 5f) {
                        Text(
                            text = stringResource(id = R.string.fail),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    } else if (grade.toFloat() >= 9f) {
                        Text(
                            text = stringResource(id = R.string.outstanding),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.pass),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    Text(
                        text = " - ${grade}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}