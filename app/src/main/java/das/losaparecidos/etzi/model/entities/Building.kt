package das.losaparecidos.etzi.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building")
data class Building(
    @PrimaryKey val id: String,
    val abbreviation: String,
    val name: String,
    val address: String
)