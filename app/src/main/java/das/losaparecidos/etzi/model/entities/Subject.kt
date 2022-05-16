package das.losaparecidos.etzi.model.entities

import java.time.LocalDate

data class Subject(
    val name: String,
    val academicYear: LocalDate,
    val degree: String,
    val type: String,
    val credits: Int,
)
