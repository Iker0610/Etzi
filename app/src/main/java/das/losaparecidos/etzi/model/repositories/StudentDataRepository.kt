package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.model.database.daos.StudentCacheDataDao
import das.losaparecidos.etzi.model.datastore.Datastore
import das.losaparecidos.etzi.model.webclients.APIClient
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
}