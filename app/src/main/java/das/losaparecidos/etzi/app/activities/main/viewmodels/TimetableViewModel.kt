package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject


@HiltViewModel
class TimetableViewModel @Inject constructor(private val studentDataRepository: StudentDataRepository) : ViewModel() {

    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }


    /*************************************************
     **                    States                   **
     *************************************************/

    private val fullTimetable: Flow<Map<String, List<Lecture>>> = studentDataRepository.getGroupedTimetable()

    val currentSelectedDay = MutableStateFlow(LocalDate.today)

    val timeTable: Flow<List<Lecture>> = fullTimetable.combine(currentSelectedDay) { timetable, selectedDay -> timetable[selectedDay.toString()] ?: emptyList() }



    /*************************************************
     **                    Events                   **
     *************************************************/

    fun onSelectedDateChange(newDate: LocalDate) {
        viewModelScope.launch {
            currentSelectedDay.emit(newDate)
        }
    }
}