package das.losaparecidos.etzi.model.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class Student(
    @PrimaryKey val ldap: String,
    val email: String,
    val name: String,
    val surname: String,
    @ColumnInfo(name = "enrolled_degree") val enrolledDegree: String,
    @Ignore val profileImage: Bitmap? = null,
) {
    constructor(
        ldap: String,
        email: String,
        name: String,
        surname: String,
        enrolledDegree: String,
    ) : this(ldap, email, name, surname, enrolledDegree, null)
}
