package das.losaparecidos.etzi.app.ui.components

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.utils.epochUTCMilliseconds
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.services.ReminderStatus
import kotlinx.datetime.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationOrCalendarDialog(
    tutorialDate: LocalDateTime,
    professor: Professor,
    notificationState: ReminderStatus,
    onNotificationClick: () -> Unit,
    onDismiss: () -> Unit
) {

    val ctx = LocalContext.current
    val tutorial = stringResource(id = R.string.tutorial)

    Dialog(onDismissRequest = onDismiss) {

        Card(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp)
        ) {

            CenteredColumn {

                CenteredColumn(
                    modifier = Modifier.padding(32.dp),
                ) {
                    Icon(Icons.Rounded.Notifications, null)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.tutorial_reminders_screen_title),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.chooseCalendarOrNotification),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    ElevatedButton(
                        shape = RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp, topStart = 16.dp, topEnd = 16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onNotificationClick()
                            onDismiss()
                        }
                    )
                    {
                        val textResource = if (notificationState != ReminderStatus.ON) R.string.activateNotification else R.string.remove_notification
                        Text(stringResource(id = textResource))
                    }

                    ElevatedButton(
                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            addTutorialOnCalendar(ctx, professor, tutorialDate, tutorial)
                            onDismiss()
                        }
                    )
                    { Text(stringResource(id = R.string.saveToCalendar)) }


                }
            }
        }
    }

}

fun addTutorialOnCalendar(ctx: Context, professor: Professor, tutorialDate: LocalDateTime, tutorial: String) {

    val startDateTime: Long = tutorialDate.epochUTCMilliseconds

    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateTime)
        .putExtra(CalendarContract.Events.TITLE, "${tutorial}: ${professor.fullName}")
        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)

    ContextCompat.startActivity(ctx, intent, null)

}
