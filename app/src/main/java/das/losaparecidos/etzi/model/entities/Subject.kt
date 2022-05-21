package das.losaparecidos.etzi.model.entities

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

// ASIGNATURA
@Immutable
@Serializable
data class Subject(
    val name: String,

    @SerialName("academic_year_start")
    val academicYear: LocalDate, // Fecha de inicio del curso
    val degree: String, // Grado: Ing Informática, etc.
    val type: String, // Troncal, optativa...
    val credits: Int,
    val course: Int, // 1º, 2º....
)
