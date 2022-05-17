import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.model.entities.LectureRoom


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureRoomInfoDialog(lectureRoom: LectureRoom, onDismiss: () -> Unit) {

    val room = LatLng(43.2634, -2.9505)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                CenteredRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(stringResource(R.string.roomInfo))

                    IconButton(onClick = onDismiss) { Icon(Icons.Rounded.Close, null) }
                }
            }
        },
        text = {


            Column(

                Modifier
                    .fillMaxWidth()) {


                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${stringResource(id = R.string.classroom)}:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary)
                        Text(text = lectureRoom.number.toString())
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${stringResource(id = R.string.floor)}:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary)
                        Text(text = lectureRoom.floor.toString())
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${stringResource(id = R.string.building)}:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary)
                        Text(text = lectureRoom.building.name)
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(shape = MaterialTheme.shapes.large),
                    cameraPositionState = CameraPositionState(
                        position = CameraPosition.fromLatLngZoom(room, 17f)
                    )
                ) { Marker(position = room) }

            }
        },
        confirmButton = {}
    )
}