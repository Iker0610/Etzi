import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.utils.epochUTCMilliseconds
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.model.entities.SubjectCall
import das.losaparecidos.etzi.model.entities.SubjectEnrollment
import kotlinx.datetime.LocalDateTime
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuarterSubjectsList(subjects: List<SubjectEnrollment>) {

    val ctx = LocalContext.current

    CenteredColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {

        subjects.forEach { subjectEnrollment ->


            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(vertical = 16.dp, horizontal = 24.dp)) {

                    CenteredRow(horizontalArrangement = Arrangement.SpaceBetween) {

                        // Poner título
                        Text(
                            text = subjectEnrollment.subject.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )

                        // Curso
                        Surface(
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            CenteredRow(
                                modifier = Modifier.padding(
                                    vertical = 4.dp,
                                    horizontal = 8.dp
                                )
                            ) {
                                Text(
                                    text = "${subjectEnrollment.subject.course}º ${stringResource(id = R.string.course)}",
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        }
                    }

                    MaterialDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Datos de la asignatura
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 8.dp)) {

                        // Por cada convocatoria
                        subjectEnrollment.subjectCalls.forEach { subjectCall ->


                            CenteredRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                                Column {

                                    if (subjectCall.callType == "Ordinaria") {
                                        Text(
                                            text = "${stringResource(id = R.string.ordinaryExam)}:",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    } else {
                                        Text(
                                            text = "${stringResource(id = R.string.extraordinaryExam)}:",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    ExamDateTime(subjectCall = subjectCall)
                                }
                                FilledTonalIconButton(onClick = { addExamOnCalendar(ctx, subjectEnrollment.subject.name, subjectCall.examDate) }) {
                                    Icon(painter = painterResource(id = R.drawable.calendar_add_on), null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun ExamDateTime(subjectCall: SubjectCall) {

    Row{
        // Fecha del examen
        Icon(
            Icons.Rounded.Event, null,
            Modifier
                .padding(end = 4.dp)
                .size(18.dp)
        )
        Text(
            text = subjectCall.examDate.format("MMM dd").uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.width(32.dp))

        // Hora del examen
        Icon(
            Icons.Rounded.Schedule, null,
            Modifier
                .padding(end = 4.dp)
                .size(18.dp)
        )
        Text(
            subjectCall.examDate.format("HH:mm"),
            style = MaterialTheme.typography.labelLarge
        )

    }

}

fun addExamOnCalendar(ctx: Context, subjectName: String, examDate: LocalDateTime) {

    val startDateTime: Long = examDate.epochUTCMilliseconds

    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateTime)
        .putExtra(CalendarContract.Events.TITLE, subjectName)
        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)


    startActivity(ctx, intent, null)


}