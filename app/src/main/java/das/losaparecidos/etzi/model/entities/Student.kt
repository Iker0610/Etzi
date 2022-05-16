package das.losaparecidos.etzi.model.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class Student(
    @PrimaryKey val ldap: String,
    val email: String,
    val name: String,
    val surname: String,
    val enrolled_degree: String,
    @Ignore val profileImage: Bitmap?,
)
