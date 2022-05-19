package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.ProfessorWithTutorials
import das.losaparecidos.etzi.model.entities.SubjectTutorial
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    private var allTutorials: List<SubjectTutorial> by mutableStateOf(emptyList())

    // Filtros
    var selectedSubject: String? by mutableStateOf(null)
        private set
    var selectedProfessors: Map<ProfessorWithTutorials, Boolean> by mutableStateOf(emptyMap())
        private set
    var startDate: LocalDate by mutableStateOf(LocalDate.today)
        private set
    var endDate: LocalDate? by mutableStateOf(null)
        private set

    var filteredTutorials: Flow<List<SubjectTutorial>> = snapshotFlow {
        allTutorials
            // Filtramos las tutorias por asignatura
            .filter { subjectTutorial -> selectedSubject == null || subjectTutorial.subjectName != selectedSubject }

            // Editamos las tutorias por asignatura que hayan pasado el filtro para filtrar las listas internas
            .map { subjectTutorial ->

                // Hacemos una copia para no editar el original
                subjectTutorial.copy(professors = subjectTutorial.professors
                    //Filtramos las tutorías con los profesores selccionados en el filtro
                    .filter { selectedProfessors.getOrDefault(it, false) }
                    .map { professor ->
                        // Copiamos las tutorias del profesor para no editar el original
                        professor.copy(tutorials = professor.tutorials
                            //Editamos las tutorías con las fechas que nos han pasado por el filtro
                            .filter { tutorial ->
                                var isValid: Boolean = (startDate <= tutorial.startDate.date)
                                endDate?.let { isValid = isValid && tutorial.startDate.date <= it }
                                return@filter isValid
                            }
                        )
                    }
                )
            }
    }

    val professorsWithTutorials: MutableList<Professor> = mutableListOf()
    val subjectTutorials: MutableList<String> = mutableStateListOf("Todas")

    var tutorials: MutableList<ProfessorWithTutorials> = mutableStateListOf()
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allTutorials = studentDataRepository.getTutorials()
            allTutorials.forEach { subjectTutorial ->
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
    fun onSelectedChange(subjectName: String?, newFromDate: LocalDate, newToDate: LocalDate, chosenProfessors: List<String>) {
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
                            if (tutorial.startDate.date in newFromDate..newToDate) {
                                tutorials.add(professorWithTutorials)
                            }
                        }
                    }
                }
            }
        }
    }

    fun onFilterChange(subjectName: String?, newFromDate: LocalDate, newToDate: LocalDate, newSelectedProfessors: Map<ProfessorWithTutorials, Boolean>) {
        selectedSubject = subjectName
        startDate = newFromDate
        endDate = newToDate
        selectedProfessors = newSelectedProfessors
    }
}