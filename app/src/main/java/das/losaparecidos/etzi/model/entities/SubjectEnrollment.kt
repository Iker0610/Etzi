package das.losaparecidos.etzi.model.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

// Matriculación de un alumno en una asignatura
@Serializable
data class SubjectEnrollment(
    val subject: Subject,
    val subgroup: Int,
    @SerialName("subject_call_attendances")
    val subjectCallAttendances: List<SubjectCallAttendance>
)

// Convocatoria
@Serializable
data class SubjectCall(
    @SerialName("academic_year")
    val callType: String, // Extraordinaria - Ordinaria
    val examDate: LocalDateTime,
)


// Participación del alumno en una convocatoria de examen de una asignatura
@Serializable
data class SubjectCallAttendance(
    @SerialName("subject_call")
    val subjectCall: SubjectCall, // Datos de la convocatoria
    val grade: String, // Nota
    val distinction: Boolean = false // Matrícula de Honor
)