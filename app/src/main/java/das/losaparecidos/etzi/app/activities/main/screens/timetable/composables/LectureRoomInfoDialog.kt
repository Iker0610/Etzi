import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.maps.android.compose.*
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.map.buildingsMapData
import das.losaparecidos.etzi.model.entities.LectureRoom


@OptIn(ExperimentalComposeUiApi::class)
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

    val room = buildingsMapData.filter { building -> building.name == lectureRoom.building.name }[0].location

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.roomInfo)) },
                actions = {
                    IconButton(onClick = onDismiss) { Icon(Icons.Rounded.Close, null) }
                }
            )
        }
    ) { paddingValues ->

        BoxWithConstraints {

            if (400.dp > maxHeight) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .padding(paddingValues)
                        .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    CenteredColumn(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceAround) {

                        CenteredColumn() {
                            Text(
                                "${stringResource(id = R.string.classroom)}:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(text = lectureRoom.number.toString(), style = MaterialTheme.typography.bodyLarge)
                        }
                        CenteredColumn() {
                            Text(
                                "${stringResource(id = R.string.floor)}:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(text = lectureRoom.floor.toString(), style = MaterialTheme.typography.bodyLarge)
                        }
                        CenteredColumn() {
                            Text(
                                "${stringResource(id = R.string.building)}:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(text = lectureRoom.building.name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    MaterialDivider(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    //Spacer(modifier = Modifier.width(16.dp))

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clip(shape = MaterialTheme.shapes.large)
                            .border(2.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.large),
                        cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(room, 17f) }
                    ) {
                        BuildingMapContent(lectureRoom = lectureRoom)
                    }


                }
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        // Aula
                        CenteredColumn() {
                            Text(
                                "${stringResource(id = R.string.classroom)}:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(text = lectureRoom.number.toString(), style = MaterialTheme.typography.bodyLarge)
                        }
                        // Planta
                        CenteredColumn() {
                            Text(
                                "${stringResource(id = R.string.floor)}:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(text = lectureRoom.floor.toString(), style = MaterialTheme.typography.bodyLarge)
                        }
                        // Edificio
                        CenteredColumn() {
                            Text(
                                "${stringResource(id = R.string.building)}:",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(text = lectureRoom.building.name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }


                    // Espacio
                    MaterialDivider(modifier = Modifier.padding(vertical = 16.dp))


                    //Mapa
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(shape = MaterialTheme.shapes.large)
                            .border(2.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.large),
                        cameraPositionState = CameraPositionState(
                            position = CameraPosition.fromLatLngZoom(room, 16f)
                        )
                    ) { BuildingMapContent(lectureRoom = lectureRoom) }
                }
            }
        }
    }
}

@Composable
private fun BuildingMapContent(lectureRoom: LectureRoom) {

    buildingsMapData.forEach { build ->

        if (build.name == lectureRoom.building.name) {
            Polygon(
                points = build.polygon,
                fillColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f),
                strokeWidth = 10f,
                strokeColor = MaterialTheme.colorScheme.tertiary,
                zIndex = 1f
            )
            Marker(rememberMarkerState(lectureRoom.building.id, build.location), title = lectureRoom.building.name)
        } else {
            Polygon(
                points = build.polygon,
                fillColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                strokeWidth = 4f,
                strokeColor = MaterialTheme.colorScheme.outline,
                strokeJointType = JointType.BEVEL
            )
        }
    }
}