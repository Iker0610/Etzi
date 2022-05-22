package das.losaparecidos.etzi.model.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@Entity(tableName = "building")
data class Building(
    @PrimaryKey val id: String,
    val abbreviation: String,
    val name: String,
    val address: String
)
