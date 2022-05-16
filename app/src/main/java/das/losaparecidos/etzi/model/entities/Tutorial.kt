package das.losaparecidos.etzi.model.entities

import java.time.LocalDateTime

data class Tutorial(
    val professor: Professor,
    val lectureRoom: LectureRoom,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)
