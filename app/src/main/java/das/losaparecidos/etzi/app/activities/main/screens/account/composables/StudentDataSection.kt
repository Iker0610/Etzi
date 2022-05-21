package das.losaparecidos.etzi.app.activities.main.screens.account.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.Student

@Composable
fun StudentDataSection(student: Student, modifier: Modifier = Modifier) {
    // Class and time
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        DataSection(stringResource(R.string.student_full_name_label), "${student.name} ${student.surname}")

        DataSection(stringResource(R.string.ldap_title), student.ldap)

        DataSection(stringResource(R.string.email_label), student.email)

        DataSection(stringResource(R.string.ldap_title), student.enrolledDegree)
    }
}

@Composable
fun DataSection(title: String, value: String) {
    Column(Modifier.fillMaxWidth()) {

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun StudentDataSectionPreview() {
    EtziTheme {
        Scaffold {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(30.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StudentDataSection(student = Student("123456", "prueba001@ikasle.ehu.eus", "Prueba", "Pruebas", "Grado de ingeniería pruebas de pruebas y sistemas de pruebas de pruebación"))
            }
        }
    }
}