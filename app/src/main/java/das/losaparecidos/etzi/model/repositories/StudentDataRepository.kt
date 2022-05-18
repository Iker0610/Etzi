package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.model.webclients.APIClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StudentDataRepository @Inject constructor(
    private val apiClient: APIClient,
) {
    suspend fun getTimeTable() = apiClient.getTimetable().groupBy { it.startDate.date.toString() }
    suspend fun getTutorials() = apiClient.getTutorials()
}