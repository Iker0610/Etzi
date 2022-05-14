package das.losaparecidos.etzi.model.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "lecture",
    primaryKeys = ["subject_name", "academic_year", "degree", "subgroup", "start_date"],
    foreignKeys = [
        ForeignKey(
            entity = Building::class,
            parentColumns = ["id"],
            childColumns = ["room_building"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = Subject::class,
            parentColumns = ["name", "academic_year", "degree"],
            childColumns = ["subject_name", "academic_year", "degree"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = Professor::class,
            parentColumns = ["email"],
            childColumns = ["professor_email"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["academic_year", "start_date", "subgroup"]),
        Index(value = ["professor_email"]),
        Index(value = ["room_building"])
    ]
)
data class Lecture(
    @ColumnInfo(name = "subject_name") val subjectName: String,
    @ColumnInfo(name = "academic_year") val academicYear: LocalDate,
    val degree: String,
    val subgroup: Int,
    @ColumnInfo(name = "professor_email") val professorEmail: String,
    @ColumnInfo(name = "room_number") val roomNumber: Int,
    @ColumnInfo(name = "room_floor") val roomFloor: Int,
    @ColumnInfo(name = "room_building") val roomBuilding: String,
    @ColumnInfo(name = "start_date") val startDate: LocalDateTime,
    @ColumnInfo(name = "end_date") val endDate: LocalDateTime
)