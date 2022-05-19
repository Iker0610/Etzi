package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.app.utils.today
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.mockdata.lectures
import das.losaparecidos.etzi.model.webclients.APIClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

// TODO: ELIMINAR CUANDO NO SEA NECESARIO
private val groupedFalseLectures = lectures.groupBy { it.startDate.date.toString() }

@Singleton
class StudentDataRepository @Inject constructor(
    private val apiClient: APIClient,
) {
    suspend fun getTimeTable(): Map<String, List<Lecture>> = apiClient.getTimetable().groupBy { it.startDate.date.toString() }
    suspend fun getTodayTimetable(): Flow<List<Lecture>> = flow {
        while (true) {
            emit(groupedFalseLectures[LocalDate.today.toString()] ?: emptyList())
            delay(1000 * 60 * 5)
        }
    }

    suspend fun getTutorials() = apiClient.getTutorials()
    suspend fun getRecord() = apiClient.getRecord()
}