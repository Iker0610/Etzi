import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
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
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.model.entities.Professor
import kotlinx.datetime.LocalDateTime
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationOrCalendarDialog(tutorialDate: LocalDateTime, professor: Professor, onDismiss: () -> Unit) {

    val ctx = LocalContext.current
    val tutorial = stringResource(id = R.string.tutorial)

    Dialog(onDismissRequest = onDismiss) {

        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp)
        ) {

            CenteredColumn {

                CenteredColumn(
                    modifier = Modifier.padding(32.dp),) {
                    Icon(Icons.Rounded.Info, null)

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

                    TextButton(
                        onClick = {
                            onDismiss
                            // TODO ACTIVAR NOTIFICACIÓN
                        })
                    { Text(stringResource(id = R.string.ativateNotification), color = MaterialTheme.colorScheme.tertiary) }

                    TextButton(
                        onClick = {
                            addTutorialOnCalendar(ctx, professor, tutorialDate, tutorial)
                            onDismiss
                        }) 
                    { Text(stringResource(id = R.string.saveToCalendar), color = MaterialTheme.colorScheme.tertiary) }


                }
            }
        }
    }

}

fun addTutorialOnCalendar(ctx: Context, professor: Professor, tutorialDate: LocalDateTime, tutorial: String) {

    val startDateTime: Long = Calendar.getInstance().run {
        set(tutorialDate.year, tutorialDate.monthNumber, tutorialDate.dayOfMonth, tutorialDate.hour, tutorialDate.minute)
        timeInMillis
    }

    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateTime)
        .putExtra(CalendarContract.Events.TITLE, "${tutorial}: ${professor.fullName}")
        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)

    ContextCompat.startActivity(ctx, intent, null)

}
