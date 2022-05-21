package das.losaparecidos.etzi.model.entities


import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.Relation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
@Immutable
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

@Immutable
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

@Immutable
@Serializable
data class SerializableLecture(
    @SerialName("subject_name") val subjectName: String,
    @SerialName("academic_year") val academicYear: LocalDate,
    val degree: String,
    val subgroup: Int,
    @SerialName("start_date") val startDate: LocalDateTime,
    @SerialName("end_date") val endDate: LocalDateTime,
    val professor: Professor,
    @SerialName("lecture_room") val lectureRoom: LectureRoom,
) {
    val lecture: Lecture by lazy {
        Lecture(
            lecture = LectureEntity(
                subjectName = subjectName,
                academicYear = academicYear,
                degree = degree,
                subgroup = subgroup,
                professorEmail = professor.email,
                roomNumber = lectureRoom.number,
                roomFloor = lectureRoom.floor,
                roomBuilding = lectureRoom.building.id,
                startDate = startDate,
                endDate = endDate
            ),
            building = lectureRoom.building,
            professor = professor
        )
    }
}