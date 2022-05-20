package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.model.entities.Student
import das.losaparecidos.etzi.model.webclients.APIClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StudentDataRepository @Inject constructor(
    private val apiClient: APIClient,
) {
    suspend fun getTimeTable() = apiClient.getTimetable().groupBy { it.startDate.date.toString() }
    suspend fun getTutorials() = apiClient.getTutorials()
    suspend fun getRecord() = apiClient.getRecord()
    suspend fun getStudentData() = Student("123456","pprueba001@ikasle.ehu.eus","Pruebas","Prueba","Grado en ingeniería de pruebas")
}