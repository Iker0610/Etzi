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
fun YearCreditsScreen(selectedTab: Int, modifier: Modifier = Modifier) {

    var total_credits = 0
    var approved_credits = 0
    var unassessed_credits = 0


    // Contar créditos
    subtectsCallAttendance.forEach { subjectCallAttendace ->

        // Sumar todos los créditos de las asignaturas aprobadas
        if (subjectCallAttendace.grade != "" && Integer.parseInt(subjectCallAttendace.grade) >= 5) {
            total_credits += subjectCallAttendace.subjectCall.subject.credits
        }

        // Obtener asiganaturas del curso seleccionado
        if (subjectCallAttendace.subjectCall.subject.course == selectedTab + 1) {

            // Si tiene una nota mayor o igual que 5, sumar créditos aprobados
            if (subjectCallAttendace.grade != "" && Integer.parseInt(subjectCallAttendace.grade) >= 5) {
                approved_credits += subjectCallAttendace.subjectCall.subject.credits
            }

            // Si la fecha de matriculación ya ha pasado y no tiene nota, marcar como sin evaluar
            else if (subjectCallAttendace.grade == ""
                && LocalDate.now()
                    .isAfter(subjectCallAttendace.subjectCall.academicYear.toJavaLocalDate())
            ) {

                unassessed_credits += subjectCallAttendace.subjectCall.subject.credits
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

        Divider(Modifier
            .fillMaxWidth(0.25f)
            .padding(vertical = 16.dp))

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


