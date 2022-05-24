package das.losaparecidos.etzi.app.ui.components

import LectureRoomInfoDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import das.losaparecidos.etzi.model.entities.LectureRoom

@Composable
fun LectureRoomInfoButton(
    lectureRoom: LectureRoom,
    modifier: Modifier = Modifier,

    ) {

    // Dialog
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        LectureRoomInfoDialog(lectureRoom = lectureRoom) { showDialog = false }
    }


    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .width(64.dp)
            .clickable { showDialog = true }
    ) {
        CenteredRow(
            modifier = Modifier.padding(
                vertical = 4.dp,
                horizontal = 8.dp
            )
        ) {
            Text(
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.75.sp, lineHeight = 20.sp),
                textAlign = TextAlign.Center,
                text = lectureRoom.fullCode,
                maxLines = 1
            )
        }
    }

}