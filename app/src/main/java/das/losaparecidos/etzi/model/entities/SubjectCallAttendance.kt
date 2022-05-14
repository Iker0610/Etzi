package das.losaparecidos.etzi.model.entities

import java.time.LocalDate

data class SubjectCallAttendance(
    val student_ldap: String,
    val subject_name: String,
    val academic_year: LocalDate,
    val degree: String,
    val call_type: String,
    val grade: String,
    val distinction: Boolean
)
