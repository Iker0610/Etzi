package das.losaparecidos.etzi.model.repositories

import android.database.sqlite.SQLiteConstraintException
import das.losaparecidos.etzi.model.database.daos.ReminderDao
import das.losaparecidos.etzi.model.datastore.Datastore
import das.losaparecidos.etzi.model.entities.LectureReminder
import das.losaparecidos.etzi.model.entities.TutorialReminder
import javax.inject.Inject


class ReminderRepository @Inject constructor(
    private val datastore: Datastore,
    private val reminderDao: ReminderDao
) {
    suspend fun addLectureReminder(lectureReminder: LectureReminder): Boolean {
        return try {
            reminderDao.addLectureReminder(lectureReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun removeLectureReminder(lectureReminder: LectureReminder): Boolean {
        return try {
            reminderDao.deleteLectureReminder(lectureReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun getAllLectureReminders() = reminderDao.getAllLectureReminders()

    fun getStudentLectureReminders(ldap: String) = reminderDao.getStudentLectureReminders(ldap)


    //------------------------------------------------------------------------------

    suspend fun addTutorialReminder(tutorialReminder: TutorialReminder): Boolean {
        return try {
            reminderDao.addTutorialReminder(tutorialReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun removeTutorialReminder(tutorialReminder: TutorialReminder): Boolean {
        return try {
            reminderDao.deleteTutorialReminder(tutorialReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun getAllTutorialReminders() = reminderDao.getAllTutorialReminders()

    fun getStudentTutorialReminders(ldap: String) = reminderDao.getStudentTutorialReminders(ldap)
}