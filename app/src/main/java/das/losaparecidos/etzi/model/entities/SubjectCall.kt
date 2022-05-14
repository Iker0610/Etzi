package das.losaparecidos.etzi.model.entities

import java.time.LocalDate
import java.time.LocalDateTime

data class SubjectCall(
    val subject_name: String,
    val academic_year: LocalDate,
    val degree: String,
    val call_type: String,
    val exam_date: LocalDateTime
)
