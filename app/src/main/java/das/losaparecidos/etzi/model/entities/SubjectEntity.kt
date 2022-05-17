package das.losaparecidos.etzi.model.entities

import androidx.room.*
import java.time.LocalDate

@Entity(
    tableName = "subject",
    primaryKeys = ["name", "academicYear", "degree", "type", "credits", "course"]
)

data class SubjectEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "academicYear") val academicYear: LocalDate,
    val degree: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "credits") val credits: Int,
    @ColumnInfo(name = "course") val course: Int
)

data class Subject(
    @Embedded val subject: SubjectEntity,
){
    @delegate:Ignore
    val name by subject::name

    @delegate:Ignore
    val academicYear by subject:: academicYear

    @delegate:Ignore
    val degree by subject:: degree

    @delegate:Ignore
    val type by subject:: type

    @delegate:Ignore
    val credits by subject:: credits

    @delegate:Ignore
    val course by subject:: course
}
