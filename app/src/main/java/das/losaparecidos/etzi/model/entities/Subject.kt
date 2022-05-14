package das.losaparecidos.etzi.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.LocalDate

@Entity(tableName = "subject", primaryKeys = ["name", "academic_year", "degree"])
data class Subject(
    val name: String,
    @ColumnInfo(name = "academic_year") val academicYear: LocalDate,
    val degree: String,
    val type: String,
    val credits: Int
)
