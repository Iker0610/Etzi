package das.losaparecidos.etzi.model.database.daos


import androidx.room.*
import das.losaparecidos.etzi.model.entities.LectureReminder
import das.losaparecidos.etzi.model.entities.TutorialReminder
import kotlinx.coroutines.flow.Flow

/**
 * DAO defining the room database access API related to alarm management
 */
@Dao
interface ReminderDao {

    @Insert
    suspend fun addLectureReminder(lectureReminder: LectureReminder)

    @Delete
    suspend fun deleteLectureReminder(lectureReminder: LectureReminder)

    @Transaction
    @Query("SELECT * FROM lecture_reminders")
    suspend fun getAllLectureReminders(): List<LectureReminder>

    @Transaction
    @Query("SELECT * FROM lecture_reminders WHERE student_ldap = :ldap")
    fun getStudentLectureReminders(ldap: String): Flow<List<LectureReminder>>


    //------------------------------------------------------------------------------


    @Insert
    suspend fun addTutorialReminder(tutorialReminder: TutorialReminder)

    @Delete
    suspend fun deleteTutorialReminder(tutorialReminder: TutorialReminder)

    @Transaction
    @Query("SELECT * FROM tutorial_reminders")
    suspend fun getAllTutorialReminders(): List<TutorialReminder>

    @Transaction
    @Query("SELECT * FROM tutorial_reminders WHERE student_ldap = :ldap")
    fun getStudentTutorialReminders(ldap: String): Flow<List<TutorialReminder>>
}

