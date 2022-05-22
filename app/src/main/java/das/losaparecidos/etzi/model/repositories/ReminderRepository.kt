package das.losaparecidos.etzi.model.repositories

import android.database.sqlite.SQLiteConstraintException
import das.losaparecidos.etzi.model.database.daos.ReminderDao
import das.losaparecidos.etzi.model.datastore.Datastore
import das.losaparecidos.etzi.model.entities.LectureReminder
import das.losaparecidos.etzi.model.entities.TutorialReminder
import javax.inject.Inject


class ReminderRepository @Inject constructor(
    private val datastore: Datastore,
    private val remainderDao: ReminderDao
) {
    suspend fun addLectureReminder(lectureReminder: LectureReminder): Boolean {
        return try {
            remainderDao.addLectureReminder(lectureReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun removeLectureReminder(lectureReminder: LectureReminder): Boolean {
        return try {
            remainderDao.deleteLectureReminder(lectureReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun getAllLectureRemainders() = remainderDao.getAllLectureRemainders()

    fun getStudentLectureRemainders(ldap: String) = remainderDao.getStudentLectureRemainders(ldap)


    //------------------------------------------------------------------------------

    suspend fun addTutorialReminder(tutorialReminder: TutorialReminder): Boolean {
        return try {
            remainderDao.addTutorialReminder(tutorialReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun removeTutorialReminder(tutorialReminder: TutorialReminder): Boolean {
        return try {
            remainderDao.deleteTutorialReminder(tutorialReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun getAllTutorialRemainders() = remainderDao.getAllTutorialRemainders()

    fun getStudentTutorialRemainders(ldap: String) = remainderDao.getStudentTutorialRemainders(ldap)
}