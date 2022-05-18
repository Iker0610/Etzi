package das.losaparecidos.etzi.app.activities.main.screens.record

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPasteOff
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import subjectEnrollments
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit) {

    val (selectedSubject, setSelectedSubject) = remember { mutableStateOf("") }    
    
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Grades.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ){  paddingValues ->

        CenteredColumn(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()) {

            var hayNotas = false

            subjectEnrollments.forEach { subjectEnrollment ->

                // Si existe la convocatoria actual, está evaluada y la nota es provisional
                if (subjectEnrollment.subjectCalls.isNotEmpty()
                    && subjectEnrollment.subjectCalls.last().subjectCallAttendances.isNotEmpty()
                    && subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].provisional) {

                    Card(onClick = {

                        // Al clicar cambiar selección
                        if (selectedSubject == subjectEnrollment.subject.name) setSelectedSubject("")
                        else setSelectedSubject(subjectEnrollment.subject.name)

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        // Si la asignatura es del curso


                        // Poner título
                        Text(text = subjectEnrollment.subject.name)
                        hayNotas = true

                        // Si está seleccionada
                        if (selectedSubject == subjectEnrollment.subject.name) {

                            Column() {
                                Row() {
                                    Text("${stringResource(id = R.string.course).capitalize()}:")
                                    Text(text = "${subjectEnrollment.subject.course}º ${stringResource(
                                        id = R.string.course
                                    )}")
                                }
                                Row() {
                                    Text(text = "${stringResource(id = R.string.call)}:")
                                    Text(text = subjectEnrollment.subjectCalls.last().callType)
                                }
                                Row() {
                                    Text(text = "${stringResource(id = R.string.grade)}:")
                                    Text(text = subjectEnrollment.subjectCalls.last().subjectCallAttendances[0].grade)
                                }
                            }
                        }
                    }
                }
            }
            if(!hayNotas){
                Spacer(modifier = Modifier.height(32.dp))
                Icon(Icons.Rounded.ContentPasteOff,
                    null,
                    modifier = Modifier.size(128.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(stringResource(id = R.string.noGrades),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }
}