package das.losaparecidos.etzi.model.entities

import java.time.LocalDateTime

data class Tutorial(
    val professor_email: String,
    val room_number: Int,
    val room_floor: Int,
    val room_building: String,
    val start_date: LocalDateTime,
    val end_date: LocalDateTime
)
