package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables

import das.losaparecidos.etzi.app.ui.components.NotificationOrCalendarDialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.LectureRoomInfoButton
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.Tutorial
import das.losaparecidos.etzi.services.ReminderStatus
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialCard(
    tutorial: Tutorial,
    professor: Professor,
    reminderStatus: ReminderStatus,
    onReminderClick: (Tutorial, Professor, ReminderStatus) -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    var showReminderDialog by rememberSaveable { mutableStateOf(false) }

    val emailSubject = stringResource(id = R.string.email_subject, tutorial.startDate.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
    val emailSalutation = stringResource(id = R.string.email_salutation)

    if (showReminderDialog) {
        NotificationOrCalendarDialog(
            tutorialDate = tutorial.startDate,
            professor = professor,
            notificationState = reminderStatus,
            onNotificationClick = { onReminderClick(tutorial, professor, reminderStatus) },
            onDismiss = { showReminderDialog = false }
        )
    }

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
                    FilledTonalIconToggleButton(
                        checked = reminderStatus == ReminderStatus.ON,
                        onCheckedChange = { showReminderDialog = true },
                        enabled = reminderStatus != ReminderStatus.UNAVAILABLE
                    ) {
                        val icon = when (reminderStatus) {
                            ReminderStatus.ON -> Icons.Rounded.NotificationsActive
                            ReminderStatus.OFF -> Icons.Rounded.NotificationsNone
                            ReminderStatus.UNAVAILABLE -> Icons.Rounded.NotificationsOff
                        }
                        Icon(icon, null)
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

                            startActivity(context, emailIntent, null)
                        }
                    ) {
                        Icon(Icons.Rounded.Mail, null, tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }//Column
}//ElevatedCard
