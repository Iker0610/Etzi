package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.now
import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.entities.LectureReminder
import das.losaparecidos.etzi.model.repositories.ReminderRepository
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import das.losaparecidos.etzi.services.ReminderManager
import das.losaparecidos.etzi.services.ReminderStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import javax.inject.Inject


@HiltViewModel
class TimetableViewModel @Inject constructor(
    studentDataRepository: StudentDataRepository,
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }


    /*************************************************
     **                    States                   **
     *************************************************/

    private val fullTimetable: Flow<Map<String, List<Lecture>>> = studentDataRepository.getGroupedTimetable()

    val currentSelectedDay = MutableStateFlow(LocalDate.today)

    val timeTable: Flow<List<Lecture>> = fullTimetable.combine(currentSelectedDay) { timetable, selectedDay -> timetable[selectedDay.toString()] ?: emptyList() }


    // REMINDERS
    private val refreshFlow: Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(1000)
        }
    }

    private val currentReminders = reminderRepository.getStudentLectureReminders()

    val lectureRemainders = combine(timeTable, currentReminders, refreshFlow) { lectures, reminders, _ ->
        val systemTZ = TimeZone.currentSystemDefault()

        lectures.associateWith { lecture ->

            val lectureReminderLimit = lecture.startDate.toInstant(systemTZ).minus(DateTimePeriod(minutes = ReminderManager.minutesBeforeReminder), systemTZ)

            when {
                lectureReminderLimit <= LocalDateTime.now.toInstant(systemTZ) -> ReminderStatus.UNAVAILABLE
                reminders.any { it.lectureRoom == lecture.lectureRoom.fullCode && it.lectureDate == lecture.startDate && it.subject == lecture.subjectName && it.degree == lecture.degree } -> ReminderStatus.ON
                else -> ReminderStatus.OFF
            }
        }
    }


    /*************************************************
     **                    Events                   **
     *************************************************/

    fun onSelectedDateChange(newDate: LocalDate) {
        viewModelScope.launch {
            currentSelectedDay.emit(newDate)
        }
    }


    // REMINDERS
    suspend fun addLectureReminder(lecture: LectureReminder) = reminderRepository.addCurrentStudentLectureReminder(lecture)
    suspend fun removeLectureReminder(lecture: LectureReminder): LectureReminder? = reminderRepository.removeCurrentUserLectureReminder(lecture)
}