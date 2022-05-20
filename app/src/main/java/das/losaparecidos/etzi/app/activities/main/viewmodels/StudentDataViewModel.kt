package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject


@HiltViewModel
class StudentDataViewModel @Inject constructor(private val studentDataRepository: StudentDataRepository) : ViewModel() {

    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }


    /*************************************************
     **                    States                   **
     *************************************************/

    var loadingData by mutableStateOf(true)
        private set

    private var fullTimeTable: Map<String, List<Lecture>> = emptyMap()

    var currentSelectedDay by mutableStateOf(LocalDate.today)
        private set

    var timeTable by mutableStateOf<List<Lecture>>(emptyList())
        private set

    init {
        Log.d("VIEWMODEL", "Se ha creado un StudentDataViewModel")

        viewModelScope.launch(Dispatchers.IO) {
            fullTimeTable = studentDataRepository.getGroupedTimeTable()
            timeTable = fullTimeTable[currentSelectedDay.toString()] ?: emptyList()

            loadingData = false
        }
    }


    /*************************************************
     **                    Events                   **
     *************************************************/

    fun onSelectedDateChange(newDate: LocalDate) {
        if (currentSelectedDay != newDate) {
            timeTable = fullTimeTable[newDate.toString()] ?: emptyList()
            currentSelectedDay = newDate
        }
    }
}