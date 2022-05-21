package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.SubjectEnrollment
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class RecordViewModel @Inject constructor(private val studentDataRepository: StudentDataRepository) :
    ViewModel() {

    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }

    /*************************************************
     **                    States                   **
     *************************************************/

    var loadingData by mutableStateOf(true)
        private set

    private var fullRecord: List<SubjectEnrollment> by mutableStateOf(emptyList())

    // Initialization
    init {
        Log.d("VIEWMODEL", "Se ha creado un RecordViewModel")

        viewModelScope.launch(Dispatchers.IO) {
            fullRecord = studentDataRepository.getRecord()
            loadingData = false
        }
    }


    val recordGroupedByCourse: Flow<Map<Int, List<SubjectEnrollment>>> = snapshotFlow { fullRecord }
        .map { recordList -> recordList.groupBy { record -> record.subject.course } }


    val provisionalSubjectGrades: Flow<List<SubjectEnrollment>> = snapshotFlow { fullRecord }
        .map { records ->
            records.filter {
                it.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.provisional ?: false
            }
        }

    val enrolledCourseSet: Flow<SortedSet<Int>> = snapshotFlow { fullRecord }
        .map { records -> records.map { it.subject.course }.toSortedSet() }

    val approvedCreditsInDegree: Flow<Int> = snapshotFlow { fullRecord }
        .map { records ->
            records.fold(0) { credits, subjectEnrollment ->
                if ((subjectEnrollment.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.grade?.toFloat() ?: 0f) >= 5f) {
                    credits + subjectEnrollment.subject.credits
                }
                else credits
            }
        }


    /*************************************************
     **                    Events                   **
     *************************************************/
//
//    fun obtainProvisionalSubjectGrades() = fullRecord.filter {
//        it.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.provisional ?: false
//    }
//
//    fun obtainEnrolledCourseSet(): SortedSet<Int> = fullRecord.map { it.subject.course }.toSortedSet()
//
//
//    fun obtainApprobedCreditsInDegree(): Int = fullRecord.fold(0) { credits, subjectEnrollment ->
//        if ((subjectEnrollment.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.grade?.toFloat() ?: 0f) >= 5f)
//            credits + subjectEnrollment.subject.credits else credits
//    }
}