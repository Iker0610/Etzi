package das.losaparecidos.etzi.model.entities

import java.time.LocalDate

data class SubjectEnrollment(
    val ldap: String,
    val subject_name: String,
    val academic_year: LocalDate,
    val degree: String,
    val subgroup: Int
)
