import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.DynamicLargeMediumTopAppBar
import das.losaparecidos.etzi.app.ui.components.showDatePicker
import das.losaparecidos.etzi.model.entities.LectureRoom


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LectureRoomInfoDialog(lectureRoom: LectureRoom, onDismiss: () -> Unit) {


    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = onDismiss,
        content = {
            DialogContent(lectureRoom, onDismiss)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogContent(lectureRoom: LectureRoom, onDismiss: () -> Unit) {

    val room = LatLng(43.2634, -2.9505)

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(id = R.string.roomInfo)) },

                actions = {
                    IconButton(onClick = onDismiss) { Icon(Icons.Rounded.Close, null) }
                }
            )
        }
    ) { paddingValues ->

        Column(

            Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${stringResource(id = R.string.classroom)}:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(text = lectureRoom.number.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${stringResource(id = R.string.floor)}:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(text = lectureRoom.floor.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${stringResource(id = R.string.building)}:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(text = lectureRoom.building.name, style = MaterialTheme.typography.bodyMedium)
                }


            }

            Spacer(modifier = Modifier.height(16.dp))

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(shape = MaterialTheme.shapes.large)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.large),
                cameraPositionState = CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(room, 17f)
                )
            ) { Marker(position = room) }

        }
    }

}