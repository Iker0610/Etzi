package das.losaparecidos.etzi.model.entities

import java.time.LocalDate
import java.time.LocalDateTime

data class SubjectCall(
    val subject: Subject,
    val academicYear: LocalDate,
    val degree: String,
    val callType: String,
    val examDate: LocalDateTime,
)
