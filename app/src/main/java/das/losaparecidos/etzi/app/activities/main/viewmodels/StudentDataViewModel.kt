package das.losaparecidos.etzi.app.activities.main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject


@HiltViewModel
class StudentDataViewModel @Inject constructor(private val studentDataRepository: StudentDataRepository) : ViewModel() {

    /*************************************************
     **                    States                   **
     *************************************************/

    private var fullTimeTable: Map<String, List<Lecture>> = emptyMap()

    init {
        viewModelScope.launch(Dispatchers.IO) { fullTimeTable = studentDataRepository.getTimeTable() }
    }

    var currentSelectedDay by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
        private set

    var timeTable by mutableStateOf<List<Lecture>>(emptyList())
        private set


    /*************************************************
     **                    Events                   **
     *************************************************/

    fun onSelectedDateChange(newDate: LocalDate) {
        if (currentSelectedDay != newDate) timeTable = fullTimeTable[newDate.toString()] ?: emptyList()
    }
}