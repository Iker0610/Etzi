package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.model.entities.Subject
import das.losaparecidos.etzi.model.entities.SubjectEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectCard(subject: SubjectEntity, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Dialog
    var showDialog by remember { mutableStateOf(false) }
    // TODO Dialogo con las notas

    ElevatedCard(modifier = modifier) {
        Row(
            Modifier
                .padding(vertical = 16.dp)
                .height(IntrinsicSize.Min)
        ) {
            CenteredColumn(
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
                    .width(64.dp)
            ) {
                Icon(Icons.Rounded.Schedule, null)
            }

            Divider(
                Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            CenteredRow(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(text = subject.name, style = MaterialTheme.typography.labelMedium)
                Surface(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .width(64.dp)

                ) {}

            }
        }
    }

}