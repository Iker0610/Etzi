package das.losaparecidos.etzi.app.activities.main.screens.record.Composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.ui.components.CenteredColumn

enum class EstadoCurso { VISIBLE, HIDDEN }

@Composable
fun CurseContainer(
    title: String,
    defaultState: EstadoCurso = EstadoCurso.HIDDEN,
    content: @Composable ColumnScope.() -> Unit
) {

    var estaVisible by rememberSaveable {
        mutableStateOf(defaultState)
    }
    LazyColumn(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues()
    ) {
        item {
            Row(
                Modifier
                    .padding(vertical = 16.dp)
                    .clickable {
                        estaVisible =
                            if (estaVisible == EstadoCurso.VISIBLE) EstadoCurso.HIDDEN else EstadoCurso.VISIBLE
                    }
            ) {
                CenteredColumn(
                    Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp)
                        .width(64.dp)
                ) {
                    Icon(Icons.Rounded.ExpandMore, null)
                }
            }

            Divider(
                Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            )
            {
                CenteredColumn(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    AnimatedVisibility(visible = estaVisible == EstadoCurso.VISIBLE) {
                        Column {
                            content()
                        }
                    }
                }
            }
        }
    }
}