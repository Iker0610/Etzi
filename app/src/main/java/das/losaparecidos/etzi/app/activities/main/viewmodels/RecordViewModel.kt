package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.SubjectEnrollment
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
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

    private var fullRecord: List<SubjectEnrollment> = emptyList()
    var recordGroupedByCourse: Map<Int, List<SubjectEnrollment>> = emptyMap()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            fullRecord = studentDataRepository.getRecord()
            recordGroupedByCourse = fullRecord.groupBy { it.subject.course }
        }
    }


    /*************************************************
     **                    Events                   **
     *************************************************/

    fun obtainFilteredRecordByCourse(course: Int) = recordGroupedByCourse.getOrDefault(course, emptyList())

    fun obtainProvisionalSubjectGrades() = fullRecord.filter {
        it.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.provisional ?: false
    }

    fun obtainEnrolledCourseSet(): SortedSet<Int> = fullRecord.map { it.subject.course }.toSortedSet()


    fun obtainApprobedCreditsInDegree(): Int = fullRecord.fold(0) { credits, subjectEnrollment ->
        if ((subjectEnrollment.subjectCalls.lastOrNull()?.subjectCallAttendances?.firstOrNull()?.grade?.toFloat() ?: 0f) >= 5f)
            credits + subjectEnrollment.subject.credits else credits
    }
}