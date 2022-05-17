package das.losaparecidos.etzi.model.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class SubjectEnrollment(
    val subject: Subject,
    @SerialName("academic_year")
    val academicYear: LocalDate,
    val degree: String,
    val subgroup: Int
)
