package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.model.database.daos.StudentCacheDataDao
import das.losaparecidos.etzi.model.webclients.APIClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StudentDataRepository @Inject constructor(
    private val apiClient: APIClient,
    private val studentCacheDataDao: StudentCacheDataDao
) {
    suspend fun getGroupedTimeTable() = apiClient.getTimetable().groupBy { it.startDate.date.toString() }
    suspend fun overwriteTimeTable() = studentCacheDataDao.overwriteTimetable(getTimeTable())
    suspend fun updateStudentData() = studentCacheDataDao.addOrUpdateStudent(getStudentData())

    private suspend fun getStudentData() = apiClient.getStudentData()
    private suspend fun getTimeTable() = apiClient.getTimetable()
    suspend fun getTutorials() = apiClient.getTutorials()
    suspend fun getRecord() = apiClient.getRecord()
}