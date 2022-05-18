package das.losaparecidos.etzi.model.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubjectCallAttendance(
    @SerialName("subject_call")
    val subjectCall: SubjectCall,
    val grade: String,
    val distinction: Boolean = false
)
