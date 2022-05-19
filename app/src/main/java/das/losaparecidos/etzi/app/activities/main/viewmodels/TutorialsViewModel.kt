package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.ProfessorWithTutorials
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

    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }

    /*************************************************
     **                    States                   **
     *************************************************/

    private var allTutorials: List<SubjectTutorial> = emptyList()
    val professorsWithTutorials: MutableList<Professor> = mutableListOf()
    val subjectTutorials: MutableList<String> = mutableStateListOf("Todas")

    var tutorials: MutableList<ProfessorWithTutorials> = mutableStateListOf()
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
            //onSelectedChange(subjectTutorials.first(),LocalDate.today,LocalDate.today.plus(7, DateTimeUnit.DAY),professorsWithTutorials)
        }
    }
    /*************************************************
     **                    Events                   **
     *************************************************/
    fun onSelectedChange(subjectName: String, newFromDate: LocalDate, newToDate: LocalDate, chosenProfessors: List<String>) {
        //if (currentSelectedDay != newDate) timeTable = fullTimeTable[newDate.toString()] ?: emptyList()
        //buscar tutorias nombre de asignatura
        allTutorials.groupBy { subjectTutorial ->
            // si no es la asignatura elegida o es todas
            if (subjectName == "Todas" || subjectName == subjectTutorial.subjectName) {
                subjectTutorial.professors.groupBy { professorWithTutorials ->
                    //Si el profesor está entre los elegidos
                    if (chosenProfessors.contains(professorWithTutorials.professor.fullName)) {
                        professorWithTutorials.tutorials.groupBy { tutorial ->
                            //Si la fecha está entre la de inicio y la de fin
                            if (tutorial.startDate.date in newFromDate..newToDate){
                                tutorials.add(professorWithTutorials)
                            }
                        }
                    }
                }
            }
        }
    }
}