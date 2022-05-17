package das.losaparecidos.etzi.model.entities

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tutorial(
    val professor: Professor,
    @SerialName("lecture_room")
    val lectureRoom: LectureRoom,
    @SerialName("start_date")
    val startDate: LocalDateTime,
    @SerialName("end_date")
    val endDate: LocalDateTime,
)
