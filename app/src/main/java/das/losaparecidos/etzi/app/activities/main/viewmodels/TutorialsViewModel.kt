package das.losaparecidos.etzi.app.activities.main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.SubjectTutorial
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject


@HiltViewModel
class TutorialsViewModel @Inject constructor(
    private val studentDataRepository: StudentDataRepository
) : ViewModel() {

    /*************************************************
     **                    States                   **
     *************************************************/

    private var allTutorials: List<SubjectTutorial> = emptyList()
    val professorsWithTutorials: MutableList<Professor> = mutableListOf()
    val subjectTutorials: MutableList<String> = mutableListOf("Todas")

    var tutorials: List<SubjectTutorial> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO){
            allTutorials = studentDataRepository.getTutorials()
            allTutorials.forEach{ subjectTutorial ->
                subjectTutorial.professors.map { professorWithTutorials ->
                    professorsWithTutorials.add(professorWithTutorials.professor)
                }
                subjectTutorials.add(subjectTutorial.subjectName)
            }
        }
    }
    /*************************************************
     **                    Events                   **
     *************************************************/
    fun onSelectedDateChange(newFromDate: LocalDate, newToDate: LocalDate) {
        //if (currentSelectedDay != newDate) timeTable = fullTimeTable[newDate.toString()] ?: emptyList()
        //buscar tutorias entre fechas
    }
    fun onSelectedSubjectChange(subjectName: String = " ") {
        //if (currentSelectedDay != newDate) timeTable = fullTimeTable[newDate.toString()] ?: emptyList()
        //buscar tutorias entre las seleccionadas.
    }
    fun onSelectedProfessorsChange(professorsNames: List<String> = emptyList()) {
        //if (currentSelectedDay != newDate) timeTable = fullTimeTable[newDate.toString()] ?: emptyList()
        //buscar tutorias entre las seleccionadas.
    }

}