import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationOrCalendarDialog(onDismiss: () -> Unit) {

    val (openDialog, setOpenDialog) = remember { mutableStateOf(true) }

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
                        .padding(horizontal = 16.dp).padding(bottom = 24.dp),
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
                            onDismiss
                            // TODO GUARDAR EN CALENDARIO
                        }) 
                    { Text(stringResource(id = R.string.saveToCalendar), color = MaterialTheme.colorScheme.tertiary) }


                }
            }
        }
    }

}
