package das.losaparecidos.etzi.app.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MaterialDivider(modifier: Modifier = Modifier) {
    androidx.compose.material3.Divider(
        modifier,
        color = MaterialTheme.colorScheme.surfaceVariant
    )
}