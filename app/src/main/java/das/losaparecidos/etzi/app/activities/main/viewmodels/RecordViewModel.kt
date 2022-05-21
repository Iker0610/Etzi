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

    private var fullRawRecord: List<SubjectEnrollment> by mutableStateOf(emptyList())

    // Initialization
    init {
        Log.d("VIEWMODEL", "Se ha creado un RecordViewModel")

        viewModelScope.launch(Dispatchers.IO) {
            fullRawRecord = studentDataRepository.getRecord()
            loadingData = false
        }
    }

    // Por cada asignatura obtenemos la matriculación más reciente
    private val lastEnrollmentPerSubjectRecords: Flow<List<SubjectEnrollment>> = snapshotFlow {
        fullRawRecord.groupBy { "${it.subject.degree}&&&&${it.subject.name}" }.mapNotNull { (_, enrollmentList) -> enrollmentList.maxByOrNull { it.subject.academicYear } }
    }

    // Por cada asignatura obtenemos solo aquellas convocatorias a las que el usuario haya atendido
    private val recordWithLastCallAttendance: Flow<List<SubjectEnrollment>> = lastEnrollmentPerSubjectRecords.map { subjectEnrollments ->
        subjectEnrollments.map { subjectEnrollment ->
            val lastCall = subjectEnrollment.subjectCalls.filter { subjectCall -> subjectCall.subjectCallAttendances.isNotEmpty() }.maxByOrNull { subjectCall -> subjectCall.examDate }
            val filteredCalls = if (lastCall != null) listOf(lastCall) else emptyList()

            subjectEnrollment.copy(subjectCalls = filteredCalls)
        }
    }

    val recordGroupedByCourse: Flow<Map<Int, List<SubjectEnrollment>>> = recordWithLastCallAttendance
        .map { recordList -> recordList.groupBy { record -> record.subject.course } }


    val provisionalSubjectGrades: Flow<List<SubjectEnrollment>> = lastEnrollmentPerSubjectRecords.map { subjectEnrollments ->
        subjectEnrollments.mapNotNull { subjectEnrollment ->
            subjectEnrollment.subjectCalls.forEach { subjectCall ->
                val provisionalGrade = subjectCall.subjectCallAttendances.firstOrNull { it.provisional }

                if (provisionalGrade != null)
                    return@mapNotNull subjectEnrollment.copy(subjectCalls = listOf(subjectCall.copy(subjectCallAttendances = listOf(provisionalGrade))))
            }
            return@mapNotNull null
        }
    }


    val enrolledCourseSet: Flow<SortedSet<Int>> = lastEnrollmentPerSubjectRecords
        .map { records -> records.map { it.subject.course }.toSortedSet() }

    val approvedCreditsInDegree: Flow<Int> = lastEnrollmentPerSubjectRecords
        .map { records ->
            records.fold(0) { credits, subjectEnrollment ->

                val isSubjectPassed = subjectEnrollment.subjectCalls
                    .flatMap { it.subjectCallAttendances }
                    .any { subjectCallAttendance -> !subjectCallAttendance.provisional && subjectCallAttendance.grade.toFloat() >= 5f }

                return@fold if (isSubjectPassed) credits + subjectEnrollment.subject.credits else credits
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