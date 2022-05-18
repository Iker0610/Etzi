import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalDate
import kotlin.math.ln


@Composable
fun YearCreditsScreen(selectedCourse: Int, modifier: Modifier = Modifier) {

    var total_credits = 0
    var approved_credits = 0
    var unassessed_credits = 0


    // Contar créditos
    subjectEnrollments.forEach { subjectEnrollment ->

        // Si está matriculado (existe una convocatoria) en la asignatura
        if (subjectEnrollment.subjectCalls.isNotEmpty()) {

            // Si NO tiene corregida o la que tiene es provisional (del curso actual)
            if ((subjectEnrollment.subjectCalls.last().subjectCallAttendances.isEmpty()
                || subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].provisional)
                && subjectEnrollment.subject.course == selectedCourse) {

                // Contar créditos como pendientes de evaluación
                unassessed_credits += subjectEnrollment.subject.credits
            }

            // Si la asignatura está aprobada y NO es provisional
            else if (Integer.parseInt(subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade) >= 5
                && !subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].provisional
            ) {
                // Sumar al total del créditos aprobados de la carrera
                total_credits += subjectEnrollment.subject.credits

                // Si la asignatura es del curso seleccionado
                if(subjectEnrollment.subject.course == selectedCourse){

                    // Sumar al total del créditos aprobados del curso
                    approved_credits += subjectEnrollment.subject.credits

                }
            }
        }
    }



CenteredColumn(
verticalArrangement = Arrangement.spacedBy(12.dp),
modifier = modifier
.fillMaxSize()
.background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = (((4.5f * ln(3.0.dp.value + 1)) + 2f) / 100f)).compositeOver(MaterialTheme.colorScheme.surface))
.verticalScroll(rememberScrollState())
.padding(horizontal = 16.dp, vertical = 32.dp)
) {

    // Créditos pendientes
    CreditCard(
        title = stringResource(id = R.string.pending),
        numCredits = 60 - approved_credits - unassessed_credits
    )


    // Créditos sin evaluar
    CreditCard(
        title = stringResource(id = R.string.unassessed),
        numCredits = unassessed_credits
    )

    // Créditos en expediente
    CreditCard(title = stringResource(id = R.string.expedient), numCredits = approved_credits)


    // Créditos totales
    CreditCard(title = stringResource(id = R.string.course_total), numCredits = 60)

    Divider(
        Modifier
            .fillMaxWidth(0.25f)
            .padding(vertical = 16.dp)
    )

    // Créditos totales de la carrera
    CreditCard(title = stringResource(id = R.string.total_degree), numCredits = total_credits)
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreditCard(title: String, numCredits: Int, modifier: Modifier = Modifier) {


    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        //border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 16.dp)
                .padding(start = 16.dp)
        ) {

            // Text

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )


            // Linea
            Divider(
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            CenteredColumn(
                Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(0.15f)
            ) {
                Text(numCredits.toString(), maxLines = 1)
            }
        }
    }

}


