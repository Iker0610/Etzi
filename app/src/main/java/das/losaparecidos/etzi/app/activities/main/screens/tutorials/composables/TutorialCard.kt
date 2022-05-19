package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialCard(tutorial: Tutorial, professor: Professor, modifier: Modifier = Modifier) {

    //1 fila por cada tarjeta, aula fecha hora (fecha como en timetable)
    //2 fila columna{fila email{fila botones}
    // fila
    val context = LocalContext.current

    val emailSubject = stringResource(id = R.string.email_subject)
    val emailSalutation = stringResource(id = R.string.email_salutation)
    // Time fomat in Tametable
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormat = DateTimeFormatter.ofPattern("dd-MM-y")

    // Dialog
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        //LectureRoomInfoDialog(lectureRoom = lecture.lectureRoom){showDialog = false}
        Log.i("info: ", "poner aqui el showDialog a false")
    }

    ElevatedCard(modifier = modifier) {
//MaterialTheme.space.small
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 14.dp)
        ) {
            // Class and time
            Column(modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()) {
                CenteredRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .width(64.dp)
                            .clickable {
                                showDialog = true
                            }

                    ) {
                        CenteredRow(
                            modifier = Modifier.padding(
                                vertical = 4.dp,
                                horizontal = 4.dp
                            )
                        ) {

                            Text(
                                textAlign = TextAlign.End,
                                text = tutorial.lectureRoom.fullCode
                            )
                        }
                    }
                    Icon(Icons.Rounded.Schedule, null)
                    Text(text = tutorial.startDate.format("MMM dd").uppercase(Locale.getDefault()), modifier = Modifier.padding(4.dp))
                    Text(tutorial.startDate.toJavaLocalDateTime().format(timeFormat) + "-" + tutorial.endDate.toJavaLocalDateTime().format(timeFormat))
                }
                Row{
                    Row(modifier = modifier.padding(8.dp),horizontalArrangement = Arrangement.SpaceBetween){
                        CenteredColumn{
                            Text(
                                "${stringResource(R.string.professor)}:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = professor.fullName,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.W400)
                            )
                            Text(
                                text = "${stringResource(R.string.email_label)}:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                professor.email,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W400)
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.SpaceAround, modifier = modifier.fillMaxWidth()) {
                        FilledTonalIconToggleButton(checked = false, onCheckedChange = { /*TODO*/ }) {
                            Icon(Icons.Rounded.NotificationsNone, null)
                        }
                        FilledTonalIconToggleButton(checked = false, onCheckedChange = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                val email = professor.email
                                type = "message/rfc822"
                                val uriText = String.format(
                                    "mailto:%s?subject=%s&body=%s",
                                    "$email,", emailSubject + tutorial.startDate.date.toString(), emailSalutation
                                )
                                data = Uri.parse(uriText)
                            }
                            startActivity(context, intent, null)

                        }) {
                            Icon(Icons.Rounded.Mail, null)
                        }
                    }
                }
            }
        }//Column
    } //Row
}//ElevatedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TutorialCardPreview() {
    EtziTheme {
        Scaffold() {
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