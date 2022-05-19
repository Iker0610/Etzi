package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.Professor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipGroup(
    professors: List<Professor> = mutableListOf(),
    onSelectedChanged: (String, Boolean) -> Unit,
) {
    FlowRow(
        mainAxisSize = SizeMode.Expand,
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSpacing = 8.dp
    ) {
        professors.forEach { professor ->
            var isSelected by rememberSaveable { mutableStateOf(true) }
            FilterChip(
                selected = isSelected,
                selectedIcon = { Icon(Icons.Rounded.Check, "Selected item " + professor.fullName) },
                onClick = {
                    isSelected = !isSelected
                    onSelectedChanged(professor.email, isSelected)
                },
                label = { Text(text = professor.fullName) },
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TutorialChipPreview() {
    EtziTheme {
        Scaffold {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(30.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipGroup(professors = mutableListOf(),
                    onSelectedChanged = { profe, selected ->
                        Log.i("profesor y seleccionado?:", "$profe $selected")
                    })
            }
        }
    }
}