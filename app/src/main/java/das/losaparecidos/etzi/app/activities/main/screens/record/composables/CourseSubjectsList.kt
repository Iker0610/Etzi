package das.losaparecidos.etzi.app.activities.main.screens.record.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.model.entities.SubjectEnrollment

@Composable
fun CourseSubjectsList(subjects: List<SubjectEnrollment>) {

    var selectedSubjectHash: Int? by rememberSaveable { mutableStateOf(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        subjects.forEach { subjectEnrollment ->
            SubjectCard(
                subjectEnrollment,
                expanded = subjectEnrollment.subject.hashCode() == selectedSubjectHash,
                onClick = { selectedSubjectHash = if (selectedSubjectHash != subjectEnrollment.subject.hashCode()) subjectEnrollment.subject.hashCode() else null }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectCard(
    subjectEnrollment: SubjectEnrollment,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        val angle: Float by animateFloatAsState(
            targetValue = if (expanded) 180F else 0F,
            animationSpec = tween(
                durationMillis = 500, // duration
                easing = FastOutSlowInEasing
            )
        )

        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CenteredRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = subjectEnrollment.subject.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    Icons.Rounded.ExpandMore,
                    contentDescription = "Expand more for detail",
                    Modifier
                        .padding(start = 16.dp)
                        .rotate(angle)
                )

            }

            // Si está seleccionada
            AnimatedVisibility(expanded) {
                // Poner info de la asignatura
                SubjectDataContent(subjectEnrollment = subjectEnrollment, modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}