package das.losaparecidos.etzi.model.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "professor")
data class Professor(
    val name: String,
    val surname: String,
    @PrimaryKey val email: String,
) {
    @delegate:Ignore
    val fullName by lazy { "$name $surname" }
}
