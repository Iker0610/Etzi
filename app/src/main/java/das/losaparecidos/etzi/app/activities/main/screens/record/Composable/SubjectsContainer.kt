package das.losaparecidos.etzi.app.activities.main.screens.record.Composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import javax.security.auth.Subject

enum class EstadoAsignaturas { VISIBLE, HIDDEN }

@Composable
fun SubjectContainer(
    title: String,
    defaultState: EstadoCurso = EstadoCurso.HIDDEN,
    content: @Composable ColumnScope.() -> Unit
) {

    var estaVisible by rememberSaveable {
        mutableStateOf(defaultState)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                estaVisible =
                    if (estaVisible == EstadoCurso.VISIBLE) EstadoCurso.HIDDEN else EstadoCurso.VISIBLE
            },
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        AnimatedVisibility(visible = estaVisible == EstadoCurso.VISIBLE) {
            Column{
                content()
            }
        }
    }
}