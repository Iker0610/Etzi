package das.losaparecidos.etzi.model.repositories

import android.graphics.Bitmap
import android.util.Log
import das.losaparecidos.etzi.model.database.daos.StudentCacheDataDao
import das.losaparecidos.etzi.model.datastore.Datastore
import das.losaparecidos.etzi.model.webclients.APIClient
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StudentDataRepository @Inject constructor(
    private val apiClient: APIClient,
    private val studentCacheDataDao: StudentCacheDataDao,
    private val datastore: Datastore,
) {
    private lateinit var profileImage: Bitmap
    fun getStudentData() = studentCacheDataDao.getStudentData()
    private suspend fun fetchStudentData() = apiClient.getStudentData()

    private suspend fun fetchTimetable() = apiClient.getTimetable()
    private fun getTimetable() = studentCacheDataDao.getTimetable()
    fun getTodayTimetable() = studentCacheDataDao.getTodayTimetable()
    fun getGroupedTimetable() = getTimetable().map { timetable -> timetable.groupBy { lecture -> lecture.startDate.date.toString() } }

    suspend fun getTutorials() = apiClient.getTutorials()
    suspend fun getRecord() = apiClient.getRecord()

    suspend fun overwriteTimetable() = studentCacheDataDao.overwriteTimetable(fetchTimetable())
    suspend fun updateStudentData() {
        studentCacheDataDao.deleteStudents()
        return studentCacheDataDao.addOrUpdateStudent(fetchStudentData())
    }
    suspend fun setUserLanguage(userLdap: String, langCode: String){
        datastore.setUserLanguage(userLdap, langCode)
    }
    fun getUserLanguage(userLdap: String): Flow<String> {
        return datastore.getUserLanguage(userLdap)
    }
    suspend fun clearUserPreferences(){
        datastore.clearPreferences()
    }
    suspend fun userProfileImage(): Bitmap {
        if (!this::profileImage.isInitialized) {
            try {
                profileImage = apiClient.getUserProfile()
            } catch (e: ResponseException) {
                Log.e("HTTP", "Couldn't get profile image.")
                e.printStackTrace()
            }
        }
        return profileImage
    }

    suspend fun setUserProfileImage(image: Bitmap): Bitmap {
        try {
            apiClient.uploadUserProfile(image)
            profileImage = image
        } catch (e: ResponseException) {
            Log.e("HTTP", "Couldn't upload profile image.")
            e.printStackTrace()
        }
        return profileImage
    }
}