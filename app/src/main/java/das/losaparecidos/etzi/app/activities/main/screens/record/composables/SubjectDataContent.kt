package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.model.entities.SubjectEnrollment

@Composable
fun SubjectDataContent(
    subjectEnrollment: SubjectEnrollment,
    modifier: Modifier = Modifier
) {

    val grade = subjectEnrollment.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.grade ?: ""


    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row() {
            Text(
                text = "${stringResource(id = R.string.type)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(0.3f)
            )
            Spacer(modifier = Modifier.weight(0.05f))

            Text(
                text = subjectEnrollment.subject.type,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.7f)
            )
        }
        Row() {
            Text(
                text = "${stringResource(id = R.string.academic_year)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(0.3f)
            )
            Spacer(modifier = Modifier.weight(0.05f))

            Text(
                text = "${subjectEnrollment.subject.academicYear.year} - ${subjectEnrollment.subject.academicYear.year+1}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.7f)
            )
        }
        Row() {
            Text(
                text = "${stringResource(id = R.string.grade)}:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(0.3f)
            )
            Spacer(modifier = Modifier.weight(0.05f))

            // Si existe la convocatoria actual, está evaluada y la nota NO es provisional
            if (subjectEnrollment.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.provisional == false) {

                val gradeAsFloat = grade.toFloat()
                val distinction = subjectEnrollment.subjectCalls.last().subjectCallAttendances.first().distinction

                val gradeText = when {
                    distinction -> "${stringResource(id = R.string.distinction)} - $grade"
                    gradeAsFloat < 5f -> "${stringResource(id = R.string.fail)} - $grade"
                    gradeAsFloat >= 9f -> "${stringResource(id = R.string.outstanding)} - $grade"
                    gradeAsFloat >= 7f -> "${stringResource(id = R.string.notable)} - $grade"
                    else -> "${stringResource(id = R.string.pass)} - $grade"
                }

                Text(
                    text = gradeText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(0.7f)
                )
            }
        }
    }
}