package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.webclients.APIClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StudentDataRepository @Inject constructor(
    private val apiClient: APIClient,
) {
    suspend fun getTimeTable(): Map<String, List<Lecture>> = apiClient.getTimetable().groupBy { it.startDate.date.toString() }
    suspend fun getTodayTimetable(): Flow<List<Lecture>> = TODO("NOT IMPLEMENTED YET")
    suspend fun getTutorials() = apiClient.getTutorials()
    suspend fun getRecord() = apiClient.getRecord()
}