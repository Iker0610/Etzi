package das.losaparecidos.etzi.app.activities.main.screens.timetable.composables

import LectureRoomInfoDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.LectureRoomInfoButton
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.mockdata.lectures
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureCard(lecture: Lecture, modifier: Modifier = Modifier) {

    // Time fomat in Tametable
    val timeFormat = "HH:mm"

    // Dialog
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        LectureRoomInfoDialog(lectureRoom = lecture.lectureRoom) { showDialog = false }
    }

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


                Icon(Icons.Rounded.Schedule, null, modifier = Modifier.padding(bottom = 8.dp))
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = lecture.endDate.format("MMM dd").uppercase(Locale.getDefault())
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(lecture.startDate.format(timeFormat))
                Text(lecture.endDate.format(timeFormat))


            }

            // Linea
            MaterialDivider(
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
                        (if (lecture.subgroup != -1) {
                            "${stringResource(R.string.subgroup)} ${lecture.subgroup}"
                        } else {
                            stringResource(R.string.masterclass)
                        }).uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )

                    LectureRoomInfoButton(lecture.lectureRoom)
                }

                // Espacio
                Spacer(modifier = Modifier.height(12.dp))

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

                        Spacer(modifier = Modifier.height(12.dp))

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
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {

                        FilledTonalIconToggleButton(checked = false, onCheckedChange = { /*TODO*/ }) {
                            Icon(Icons.Rounded.NotificationsNone, null)
                        }
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
                lectures.forEach { lecture -> LectureCard(lecture) }
            }
        }
    }
}