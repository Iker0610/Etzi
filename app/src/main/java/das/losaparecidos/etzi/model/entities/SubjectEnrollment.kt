package das.losaparecidos.etzi.model.entities

import java.time.LocalDate

data class SubjectEnrollment(
    val subject: Subject,
    val academicYear: LocalDate,
    val degree: String,
    val subgroup: Int
)
