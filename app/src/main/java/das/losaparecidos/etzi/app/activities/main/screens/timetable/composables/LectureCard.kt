package das.losaparecidos.etzi.app.activities.main.screens.timetable.composables

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.Lecture
import lectures
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.time.LocalDate as LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureCard(lecture: Lecture, modifier: Modifier = Modifier) {

    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val context = LocalContext.current

    ElevatedCard(modifier = modifier) {

        Row(
            Modifier
                .padding(vertical = 16.dp)
                .height(IntrinsicSize.Min)
        ) {

            // Time
            CenteredColumn(
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
                    .width(64.dp)

            ) {
                Icon(Icons.Rounded.Schedule, null, modifier = Modifier.padding(bottom = 16.dp))
                Text(
                    lecture.startDate.format(timeFormat),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(lecture.endDate.format(timeFormat))
            }

            // Linea
            Divider(
                Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            // Textos
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // Información de la clase
                CenteredRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        "${stringResource(R.string.subgroup)}: ${lecture.subgroup}".toUpperCase(
                            Locale.getDefault()
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )

                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .width(64.dp)
                            .clickable {
                                Toast
                                    .makeText(context, "MAPA", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    ) {

                        CenteredRow(
                            modifier = Modifier.padding(
                                vertical = 4.dp,
                                horizontal = 8.dp
                            )
                        ) {

                            Text(
                                textAlign = TextAlign.End,
                                text = lecture.lectureRoom.fullCode
                            )
                        }
                    }

                }

                // Espacio
                Spacer(modifier = Modifier.height(8.dp))

                CenteredRow(horizontalArrangement = Arrangement.SpaceBetween) {

                    CenteredColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Asignatura
                        Text(
                            text = "${stringResource(R.string.subject)}:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            lecture.subjectName,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Profesor(a)
                        Text(
                            "${stringResource(R.string.professor)}:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = lecture.professor.fullName,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.W400)
                        )
                    }

                    // Botones
                    CenteredColumn(
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Rounded.Notifications, null)

                        }
                        /*
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Rounded.Map, null)

                        }
                        */
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LectureCardPreview() {


    EtziTheme {
        Scaffold() {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(30.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                lectures.forEach { lecture ->
                    LectureCard(lecture)
                }
            }

        }

    }

}