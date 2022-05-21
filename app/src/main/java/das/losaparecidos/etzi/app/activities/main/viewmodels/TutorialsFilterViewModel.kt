package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.ProfessorWithTutorials
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus


class TutorialsFilterViewModel(
    currentSelectedSubject: String? = null,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    selectedProfessors: Map<ProfessorWithTutorials, Boolean>,
    //professorList: List<Pro> = emptyList()
) : ViewModel() {

    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }

    /*************************************************
     **                    States                   **
     *************************************************/

    var currentSelectedSubject: String by mutableStateOf(currentSelectedSubject ?: "-")
    var startDate: LocalDate by mutableStateOf(startDate ?: LocalDate.today)
        private set
    var endDate: LocalDate by mutableStateOf(endDate ?: LocalDate.today.plus(7, DateTimeUnit.DAY))
        private set

    val selectedProfessors: MutableMap<ProfessorWithTutorials, Boolean> = mutableStateMapOf<ProfessorWithTutorials, Boolean>().apply { putAll(selectedProfessors) }

    val dateRange: Pair<LocalDate, LocalDate> get() = Pair(startDate, endDate)


    /*************************************************
     **                    Events                   **
     *************************************************/

    fun onDateRangeChange(newStartDate: LocalDate, newEndDate: LocalDate) {
        startDate = newStartDate
        endDate = newEndDate
    }

    fun onProfessorToggle(professor: ProfessorWithTutorials) {
        if (professor in selectedProfessors) {
            selectedProfessors[professor] = !selectedProfessors[professor]!!
        }
    }
}