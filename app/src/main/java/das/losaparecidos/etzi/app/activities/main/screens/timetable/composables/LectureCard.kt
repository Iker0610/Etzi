package das.losaparecidos.etzi.app.activities.main.screens.timetable.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.LectureEntity
import lectures
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureCard(lecture: LectureEntity) {

    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    ElevatedCard() {

        Row(
            Modifier
                .padding(vertical = 16.dp)
                .height(IntrinsicSize.Min)
        ) {

            // Time
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
                    .width(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween

                ) {
                Text(lecture.startDate.format(timeFormat))
                Text(lecture.endDate.format(timeFormat))
            }

            // Linea
            Divider(
                Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            // Info
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(lecture.subjectName)

                Divider(Modifier.padding(vertical = 8.dp).height(0.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {

                        Text(stringResource(R.string.subgroup) + ": " + lecture.subgroup)

                        Text(
                            textAlign = TextAlign.End,
                            text = "P" + lecture.roomFloor + lecture.roomBuilding + lecture.roomNumber
                        )


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