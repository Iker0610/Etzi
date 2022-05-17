package das.losaparecidos.etzi.model.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Subject(
    val name: String,

    @SerialName("academic_year")
    val academicYear: LocalDate,
    val degree: String,
    val type: String,
    val credits: Int,
    val course: Int,
)
