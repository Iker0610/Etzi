import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.model.mockdata.subjects
import java.time.LocalDate
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsCard(selectedTab: Int) {

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
                && LocalDate.now().isAfter(subjectCallAttendace.subjectCall.academicYear)) {

                unassessed_credits += subjectCallAttendace.subjectCall.subject.credits
            }

        }
    }

    // Créditos pendientes
    myCard(
        title = stringResource(id = R.string.pending),
        numCredits = 60 - approved_credits - unassessed_credits
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Créditos sin evaluar
    myCard(title = stringResource(id = R.string.unassessed), numCredits = unassessed_credits)
    Spacer(modifier = Modifier.height(16.dp))

    // Créditos en expediente
    myCard(title = stringResource(id = R.string.expedient), numCredits = approved_credits)
    Spacer(modifier = Modifier.height(16.dp))

    // Créditos totales
    myCard(title = stringResource(id = R.string.course_total), numCredits = 60)
    Divider(
        Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(1.dp)
    )

    // Créditos totales de la carrera
    myCard(title = stringResource(id = R.string.total_degree), numCredits = total_credits)
    Spacer(modifier = Modifier.height(32.dp))


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myCard(title: String, numCredits: Int) {


    ElevatedCard(
        modifier = Modifier
            .fillMaxSize()
            .height(64.dp)
    ) {

        Row() {

            // Text
            CenteredColumn(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(title, modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center)
            }

            // Linea
            Divider(
                Modifier
                    .padding(12.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )

            CenteredColumn(
                Modifier
                    .weight(0.2f)
                    .fillMaxHeight(), horizontalAlignment = Alignment.Start
            ) {
                Text(
                    numCredits.toString(),
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.End
                )
            }

        }
    }

}


