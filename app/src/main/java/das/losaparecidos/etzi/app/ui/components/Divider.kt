package das.losaparecidos.etzi.app.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MaterialDivider(modifier: Modifier = Modifier, startIndent: Dp = 0.dp) {
    androidx.compose.material3.Divider(
        modifier,
        startIndent = startIndent,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        thickness = 0.6.dp
    )
}