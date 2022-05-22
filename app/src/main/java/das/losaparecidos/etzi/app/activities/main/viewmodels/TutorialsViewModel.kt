package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.now
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.TutorialReminder
import das.losaparecidos.etzi.model.entities.ProfessorWithTutorials
import das.losaparecidos.etzi.model.entities.SubjectTutorial
import das.losaparecidos.etzi.model.repositories.ReminderRepository
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import das.losaparecidos.etzi.services.ReminderManager
import das.losaparecidos.etzi.services.ReminderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TutorialsViewModel @Inject constructor(
    private val studentDataRepository: StudentDataRepository,
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }

    /*************************************************
     **                    States                   **
     *************************************************/

    var loadingData by mutableStateOf(true)
        private set

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
            .filter { subjectTutorial -> selectedSubject == null || subjectTutorial.subjectName == selectedSubject }

            // Editamos las tutorias por asignatura que hayan pasado el filtro para filtrar las listas internas
            .mapNotNull { subjectTutorial ->
                //Filtramos las tutorías con los profesores seleccionados en el filtro
                val filteredProfessors = subjectTutorial.professors
                    //Filtramos las tutorías con los profesores selccionados en el filtro
                    .filter { selectedProfessors.getOrDefault(it, false) }
                    .mapNotNull { professor ->
                        // Copiamos las tutorias del profesor para no editar el original
                        val filteredTutorials = professor.tutorials
                            //Editamos las tutorías con las fechas que nos han pasado por el filtro
                            .filter { tutorial ->
                                var isValid: Boolean = (startDate <= tutorial.startDate.date)
                                endDate?.let { isValid = isValid && tutorial.startDate.date <= it }
                                return@filter isValid
                            }

                        if (filteredTutorials.isNotEmpty()) professor.copy(tutorials = filteredTutorials) else null
                    }

                if (filteredProfessors.isNotEmpty()) subjectTutorial.copy(professors = filteredProfessors) else null
            }
    }

    var subjectList: Set<String> by mutableStateOf(setOf())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allTutorials = studentDataRepository.getTutorials()

            // Load data transformed data
            subjectList = allTutorials.map { it.subjectName }.toSortedSet()
            selectedProfessors = allTutorials.flatMap { subject -> subject.professors }.associateWith { true }

            loadingData = false
        }
    }

    // REMINDERS
    private val refreshFlow: Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(1000)
        }
    }

    private val currentReminders = reminderRepository.getStudentTutorialReminders()

    val tutorialRemainderStates = combine(filteredTutorials, currentReminders, refreshFlow) { tutorials, reminders, _ ->
        val systemTZ = TimeZone.currentSystemDefault()

        tutorials
            .flatMap { subjectTutorial -> subjectTutorial.professors }.associate { it.professor to it.tutorials }
            .map { (professor, tutorials) ->
                tutorials.associate { tutorial ->
                    val tutorialReminderLimit = tutorial.startDate.toInstant(systemTZ).minus(DateTimePeriod(minutes = ReminderManager.minutesBeforeReminder), systemTZ)

                    "${professor.email}&&${tutorial.startDate}" to when {
                        tutorialReminderLimit <= LocalDateTime.now.toInstant(systemTZ) -> ReminderStatus.UNAVAILABLE
                        reminders.any { it.tutorialDate == tutorial.startDate && it.professorEmail == professor.email } -> ReminderStatus.ON
                        else -> ReminderStatus.OFF
                    }
                }
            }
            .fold(mutableMapOf<String, ReminderStatus>()) { outMap, map -> outMap.apply { putAll(map) } }
    }


    /*************************************************
     **                    Events                   **
     *************************************************/

    fun onFilterChange(subjectName: String?, newFromDate: LocalDate, newToDate: LocalDate?, newSelectedProfessors: Map<ProfessorWithTutorials, Boolean>) {
        selectedSubject = subjectName
        startDate = newFromDate
        endDate = newToDate
        selectedProfessors = newSelectedProfessors
    }


    // REMAINDERS
    suspend fun addTutorialReminder(tutorial: TutorialReminder) = reminderRepository.addCurrentStudentTutorialReminder(tutorial)
    suspend fun removeTutorialReminder(tutorial: TutorialReminder): TutorialReminder? = reminderRepository.removeCurrentUserTutorialReminder(tutorial)
}