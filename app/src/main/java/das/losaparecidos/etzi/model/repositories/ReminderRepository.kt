package das.losaparecidos.etzi.model.repositories

import android.database.sqlite.SQLiteConstraintException
import das.losaparecidos.etzi.model.database.daos.ReminderDao
import das.losaparecidos.etzi.model.datastore.Datastore
import das.losaparecidos.etzi.model.entities.LectureReminder
import das.losaparecidos.etzi.model.entities.TutorialReminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class ReminderRepository @Inject constructor(
    private val datastore: Datastore,
    private val reminderDao: ReminderDao
) {
    private val loggedUser = datastore.getLastLoggedUserFlow()

    suspend fun addLectureReminder(lectureReminder: LectureReminder): Boolean {
        return try {
            reminderDao.addLectureReminder(lectureReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun addCurrentStudentLectureReminder(lectureReminder: LectureReminder): LectureReminder? {
        val currentUserLdap = loggedUser.first()?.ldap ?: return null

        val newLectureReminder = lectureReminder.copy(studentLdap = currentUserLdap)
        return if (addLectureReminder(newLectureReminder)) newLectureReminder else null
    }

    suspend fun removeLectureReminder(lectureReminder: LectureReminder): Boolean {
        return try {
            reminderDao.deleteLectureReminder(lectureReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun removeCurrentUserLectureReminder(lectureReminder: LectureReminder): LectureReminder? {
        val currentUserLdap = loggedUser.first()?.ldap ?: return null
        val newLectureReminder = lectureReminder.copy(studentLdap = currentUserLdap)
        return if (removeLectureReminder(newLectureReminder)) newLectureReminder else null
    }


    suspend fun getAllLectureReminders() = reminderDao.getAllLectureReminders()

    fun getStudentLectureReminders(): Flow<List<LectureReminder>> =
        runBlocking { return@runBlocking loggedUser.first()?.let { reminderDao.getStudentLectureReminders(it.ldap) } ?: emptyFlow() }


    //------------------------------------------------------------------------------

    suspend fun addTutorialReminder(tutorialReminder: TutorialReminder): Boolean {
        return try {
            reminderDao.addTutorialReminder(tutorialReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun addCurrentTutorialReminder(tutorialReminder: TutorialReminder): TutorialReminder? {
        val currentUserLdap = loggedUser.first()?.ldap ?: return null

        val newTutorialReminder = tutorialReminder.copy(studentLdap = currentUserLdap)
        return if (addTutorialReminder(newTutorialReminder)) newTutorialReminder else null
    }

    suspend fun removeTutorialReminder(tutorialReminder: TutorialReminder): Boolean {
        return try {
            reminderDao.deleteTutorialReminder(tutorialReminder)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun removeCurrentUserTutorialReminder(tutorialReminder: TutorialReminder): TutorialReminder? {
        val currentUserLdap = loggedUser.first()?.ldap ?: return null
        val newTutorialReminder = tutorialReminder.copy(studentLdap = currentUserLdap)
        return if (removeTutorialReminder(newTutorialReminder)) newTutorialReminder else null
    }


    suspend fun getAllTutorialReminders() = reminderDao.getAllTutorialReminders()

    fun getStudentTutorialReminders(ldap: String): Flow<List<TutorialReminder>> =
        runBlocking { return@runBlocking loggedUser.first()?.let { reminderDao.getStudentTutorialReminders(it.ldap) } ?: emptyFlow() }
}