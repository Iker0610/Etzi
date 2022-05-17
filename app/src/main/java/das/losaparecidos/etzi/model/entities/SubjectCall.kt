package das.losaparecidos.etzi.model.entities

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubjectCall(
    val subject: Subject,
    @SerialName("academic_year")
    val academicYear: LocalDate,
    val degree: String,
    val callType: String,
    val examDate: LocalDateTime,
)
