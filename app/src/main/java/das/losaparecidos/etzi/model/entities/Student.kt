package das.losaparecidos.etzi.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "student")
data class Student(
    @PrimaryKey val ldap: String,
    val email: String,
    val name: String,
    val surname: String,

    @SerialName("enrolled_degree")
    @ColumnInfo(name = "enrolled_degree")
    val enrolledDegree: String,
)
