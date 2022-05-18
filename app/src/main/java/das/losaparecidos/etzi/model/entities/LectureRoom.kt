package das.losaparecidos.etzi.model.entities

import kotlinx.serialization.Serializable

@Serializable
data class LectureRoom(
    val number: Int,
    val floor: Int,
    val building: Building,
) {
    val fullCode get() = "P$floor${building.abbreviation}$number"
}
