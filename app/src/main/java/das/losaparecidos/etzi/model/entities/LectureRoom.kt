package das.losaparecidos.etzi.model.entities

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class LectureRoom(
    val number: Int,
    val floor: Int,
    val building: Building,
) {
    val fullCode get() = "P$floor${building.abbreviation}$number"
}
