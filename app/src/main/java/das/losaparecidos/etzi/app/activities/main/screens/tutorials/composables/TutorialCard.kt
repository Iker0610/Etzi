package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.LectureRoomInfoButton
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.app.utils.now
import das.losaparecidos.etzi.model.entities.Building
import das.losaparecidos.etzi.model.entities.LectureRoom
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.Tutorial
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialCard(tutorial: Tutorial, professor: Professor, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val emailSubject = stringResource(id = R.string.email_subject, tutorial.startDate.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
    val emailSalutation = stringResource(id = R.string.email_salutation)

    ElevatedCard(modifier = modifier) {

        // Class and time
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 24.dp)
        ) {
            CenteredRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LectureRoomInfoButton(tutorial.lectureRoom)

                CenteredRow(horizontalArrangement = Arrangement.End) {
                    Icon(
                        Icons.Rounded.Event, null,
                        Modifier
                            .padding(end = 4.dp)
                            .size(18.dp)
                    )
                    Text(
                        text = tutorial.startDate.format("MMM dd").uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(Modifier.width(12.dp))

                    Icon(
                        Icons.Rounded.Schedule, null,
                        Modifier
                            .padding(end = 4.dp)
                            .size(18.dp)
                    )
                    Text(
                        "${tutorial.startDate.format("HH:mm")} - ${tutorial.endDate.format("HH:mm")}",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            MaterialDivider()

            CenteredRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${stringResource(R.string.professor)}:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = professor.fullName,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${stringResource(R.string.email_label)}:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        professor.email,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(verticalArrangement = Arrangement.SpaceAround) {
                    FilledTonalIconToggleButton(checked = false, onCheckedChange = { /*TODO*/ }) {
                        Icon(Icons.Rounded.NotificationsNone, null)
                    }

                    IconButton(
                        onClick = {
                            val selectorIntent = Intent(Intent.ACTION_SENDTO).apply { data = Uri.parse("mailto:") }

                            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                data = Uri.parse("mailto:")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(professor.email))
                                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                                putExtra(Intent.EXTRA_TEXT, emailSalutation)
                                selector = selectorIntent;
                            }

                            startActivity(context,emailIntent, null)
                        }
                    ) {
                        Icon(Icons.Rounded.Mail, null, tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }//Column
}//ElevatedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TutorialCardPreview() {
    EtziTheme {
        Scaffold {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(30.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TutorialCard(
                    tutorial = Tutorial(
                        LectureRoom(2, 3, Building("2", "IM", "Euskal herriko univertsitatea", "diresió")),
                        LocalDateTime.now,
                        LocalDateTime.now
                    ), Professor("Iker", "Sobrón", "iker.sobron@ehu.eus")
                )
            }

        }

    }

}