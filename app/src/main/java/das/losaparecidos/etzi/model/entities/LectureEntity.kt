package das.losaparecidos.etzi.model.entities


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.Relation
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
            entity = Professor::class,
            parentColumns = ["email"],
            childColumns = ["professor_email"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["academic_year", "start_date", "subgroup"]),
        Index(value = ["professor_email"]),
        Index(value = ["room_building"])
    ]
)
data class LectureEntity(
    @ColumnInfo(name = "subject_name") val subjectName: String,
    @ColumnInfo(name = "academic_year") val academicYear: LocalDate,
    val degree: String,
    val subgroup: Int,
    @ColumnInfo(name = "professor_email") val professorEmail: String,
    @ColumnInfo(name = "room_number") val roomNumber: Int,
    @ColumnInfo(name = "room_floor") val roomFloor: Int,
    @ColumnInfo(name = "room_building") val roomBuilding: String,
    @ColumnInfo(name = "start_date") val startDate: LocalDateTime,
    @ColumnInfo(name = "end_date") val endDate: LocalDateTime,
)


data class Lecture(
    @Embedded val lecture: LectureEntity,

    @Relation(parentColumn = "room_building", entityColumn = "id")
    val building: Building,

    @Relation(parentColumn = "professor_email", entityColumn = "email")
    val professor: Professor,

    ) {
    @Ignore
    val lectureRoom = LectureRoom(lecture.roomNumber, lecture.roomFloor, building)

    @delegate:Ignore
    val subjectName by lecture::subjectName

    @delegate:Ignore
    val degree by lecture::degree

    @delegate:Ignore
    val subgroup by lecture::subgroup

    @delegate:Ignore
    val academicYear by lecture::academicYear

    @delegate:Ignore
    val startDate by lecture::startDate

    @delegate:Ignore
    val endDate by lecture::endDate
}