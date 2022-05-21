package das.losaparecidos.etzi.model.entities

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Immutable
@Serializable
@Entity(
    tableName = "lecture_reminders",
    primaryKeys = ["student_ldap", "lecture_room", "subject", "degree", "lecture_date"],
)
data class LectureReminder(
    @ColumnInfo(name = "student_ldap")
    val studentLdap: String,
    @ColumnInfo(name = "lecture_room")
    val lectureRoom: String,
    val subject: String,
    val degree: String,
    @ColumnInfo(name = "lecture_date")
    val lectureDate: LocalDateTime,
)


@Immutable
@Serializable
@Entity(
    tableName = "tutorial_reminders",
    primaryKeys = ["student_ldap", "lecture_room", "subject", "degree", "lecture_date"],
)
data class TutorialReminder(
    @ColumnInfo(name = "student_ldap")
    val studentLdap: String,
    @ColumnInfo(name = "professor_full_name")
    val professorFullName: String,
    @ColumnInfo(name = "professor_email")
    val professorEmail: String,
    @ColumnInfo(name = "lecture_room")
    val lectureRoom: String,
    @ColumnInfo(name = "tutorial_date")
    val tutorialDate: LocalDateTime,
)