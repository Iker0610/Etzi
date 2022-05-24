package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FilterChipGroup(
    items: Map<T, Boolean>,
    itemToStringMapper: (T) -> String = { it.toString() },
    onItemToggle: (T) -> Unit,
) {
    FlowRow(
        mainAxisSize = SizeMode.Expand,
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSpacing = 8.dp
    ) {
        items.forEach { (item, state) ->
            val itemText = itemToStringMapper(item)
            FilterChip(
                selected = state,
                selectedIcon = { Icon(Icons.Rounded.Check, "Selected item $itemText") },
                onClick = { onItemToggle(item) },
                label = { Text(text = itemText) },
            )
        }
    }
}
