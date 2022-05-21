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

                CenteredRow(horizontalArrangement = Arrangement.SpaceBetween) {

                    // Poner título
                    Text(
                        text = subjectEnrollment.subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(top = 16.dp)
                            .weight(1f)
                    )

                    // Curso
                    Surface(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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

                // Datos de la asignatura
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 16.dp)
                ) {

                    // Por cada convocatoria
                    subjectEnrollment.subjectCalls.forEach { subjectCall ->

                        Spacer(modifier = Modifier.height(24.dp))

                        CenteredRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                            Column() {

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

                                Spacer(modifier = Modifier.height(8.dp))

                                ExamDateTime(subjectCall = subjectCall)
                            }
                            IconButton(onClick = { addExamOnCalendar(ctx, subjectEnrollment.subject.name, subjectCall.examDate) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.calendar_add_on), null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(32.dp, 32.dp)
                                )
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

    CenteredRow(
        //modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Spacer(Modifier.width(8.dp))

        // Fecha del examen
        Icon(
            Icons.Rounded.Event, null,
            Modifier
                .padding(end = 4.dp)
                .size(18.dp)
        )
        Text(
            text = subjectCall.examDate.format("MMM dd").uppercase(java.util.Locale.getDefault()),
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

    val startDateTime: Long = Calendar.getInstance().run {
        set(examDate.year, examDate.monthNumber-1, examDate.dayOfMonth, examDate.hour, examDate.minute)
        timeInMillis
    }

    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateTime)
        .putExtra(CalendarContract.Events.TITLE, subjectName)
        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)


    startActivity(ctx, intent, null)


}