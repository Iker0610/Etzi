package das.losaparecidos.etzi.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "professor")
data class Professor(
    val name: String,
    val surname: String,
    @PrimaryKey val email: String
)
