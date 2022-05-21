package das.losaparecidos.etzi.model.entities

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Tutorial(
    @SerialName("lecture_room")
    val lectureRoom: LectureRoom,
    @SerialName("start_date")
    val startDate: LocalDateTime,
    @SerialName("end_date")
    val endDate: LocalDateTime,
)

@Immutable
@Serializable
data class ProfessorWithTutorials(
    val name: String,
    val surname: String,
    val email: String,
    val tutorials: List<Tutorial>
) {
    val fullName by lazy { "$name $surname" }

    val professor: Professor by lazy {
        Professor(name, surname, email)
    }
}

@Immutable
@Serializable
data class SubjectTutorial(
    @SerialName("name")
    val subjectName: String,
    val professors: List<ProfessorWithTutorials>
)