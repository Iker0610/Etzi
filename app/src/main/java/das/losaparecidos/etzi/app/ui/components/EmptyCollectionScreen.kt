package das.losaparecidos.etzi.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyCollectionScreen(
    icon: ImageVector,
    message: String,
    modifier: Modifier = Modifier
) {
    CenteredBox(
        modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Card {
            BoxWithConstraints {
                if (400.dp <= maxHeight) {
                    CenteredColumn(
                        Modifier.padding(horizontal = 32.dp, vertical = 64.dp)
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(text = message, style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
                    }
                } else {
                    CenteredRow(
                        Modifier.padding(horizontal = 64.dp, vertical = 32.dp)
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(32.dp))
                        Text(text = message, style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}