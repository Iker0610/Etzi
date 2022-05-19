package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDropdownMenu(asignaturas: List<String>, modifier: Modifier = Modifier, onSubjectSelected: (String) -> Unit) {
    val default = 0
    var expanded by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf(asignaturas[default]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = selectedSubject,
            onValueChange = { onSubjectSelected(selectedSubject) },
            label = { Text(stringResource(id = R.string.subject)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            asignaturas.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedSubject = selectionOption
                        expanded = false
                    },
                    text = { Text(text = selectionOption) },

                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SubjectsMenuPreview() {
    EtziTheme {
        SubjectDropdownMenu(asignaturas = listOf("Asignatura 1", "Asignatura 2", "Asignatura 3"), onSubjectSelected = {})
    }
}