package das.losaparecidos.etzi.model.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

// Matriculación de un alumno en una asignatura
@Serializable
data class SubjectEnrollment(
    val subject: Subject,
    val subgroup: Int,
    @SerialName("subject_calls")
    val subjectCalls: List<SubjectCall>
)

// Convocatoria
@Serializable
data class SubjectCall(
    @SerialName("call_type")
    val callType: String, // Extraordinaria - Ordinaria
    @SerialName("exam_date")
    val examDate: LocalDateTime,
    @SerialName("subject_call_attendances")
    val subjectCallAttendances: List<SubjectCallAttendance>
)


// Participación del alumno en una convocatoria de examen de una asignatura
@Serializable
data class SubjectCallAttendance(
    val grade: String, // Nota
    val distinction: Boolean = false, // Matrícula de Honor
    val provisional: Boolean
)