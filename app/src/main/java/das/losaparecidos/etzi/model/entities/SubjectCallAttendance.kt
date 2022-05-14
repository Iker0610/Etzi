package das.losaparecidos.etzi.model.entities

data class SubjectCallAttendance(
    val subjectCall: SubjectCall,
    val grade: String,
    val distinction: Boolean = false
)
