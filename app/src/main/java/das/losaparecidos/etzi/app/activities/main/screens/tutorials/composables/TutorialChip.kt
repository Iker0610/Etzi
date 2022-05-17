package das.losaparecidos.etzi.app.activities.main.screens.tutorials.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.Subject
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipGroup(
    modifier: Modifier = Modifier,
    professors: List<Professor> = listOf(),
    selectedProfessor: Professor? = null,
    onSelectedChanged: (String) -> Unit = {},
) {
    var isSelected = false
    Column(modifier = Modifier.padding(8.dp)) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.height(120.dp)
        ){
            items(professors){ professor ->
                FilterChip(
                    selected = isSelected,
                    selectedIcon = {Icon(Icons.Rounded.Check, "Selected item " + professor.fullName)},
                    onClick = {
                        isSelected = !isSelected
                        onSelectedChanged(professor.email)
                    },
                    label = {Text(text = professor.fullName)},
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TutorialChipPreview() {
    EtziTheme {
        Scaffold{
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(30.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipGroup(professors = listOf(
                    Professor("Iker", "Sobrón","iker.sobron@ehu.eus"),
                    Professor("Alicia", "Pérez", "alicia.perez@ehu.eus"),
                    Professor("Aitziber", "Atutxa", "aitziber.atutxa@ehu.eus"),
                    Professor("Koldobika", "Gojenola", "koldo.gojenola@ehu.eus"),
                    Professor("Ainhoa", "Yera", "ainhoa.yera@ehu.eus"),
                    Professor("Begoña", "blanco", "begona.blanco@ehu.eus"),
                    Professor("Iñigo", "Mendialdua", "inigo.mendialdua@ehu.eus"),
                    Professor("Mikel", "Villamañe", "mikel.v@ehu.eus"),
                    Professor("Pepe", "Perez", "pepe.perez@ehu.eus"),
                    Professor("Unprofe", "Más", "unprofe.mas@ehu.eus"),
                    Professor("Otroprofe", "Más", "Otroprofe.mas@ehu.eus"),
                   ))
            }
        }
    }
}