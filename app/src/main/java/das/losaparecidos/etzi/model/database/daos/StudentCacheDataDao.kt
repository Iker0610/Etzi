package das.losaparecidos.etzi.model.database.daos

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import das.losaparecidos.etzi.model.entities.*
import kotlinx.coroutines.flow.Flow


/**
 * DAO defining the room database access API related to Visit Card Data.
 */
@Dao
interface StudentCacheDataDao {

    // INSERTS

    @Insert
    suspend fun addStudent(student: Student)

    @Insert
    suspend fun addProfessor(professor: Professor)

    @Insert
    suspend fun addBuilding(building: Building)

    @Insert
    suspend fun addLectureEntity(lecture: LectureEntity)

    @Insert
    suspend fun addLecture(lecture: Lecture) {
        return try {
            addOrUpdateBuilding(lecture.building)
            addOrUpdateProfessor(lecture.professor)
            addLectureEntity(lecture.lecture)
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
        }
    }

    @Transaction
    suspend fun addTimetable(timetable: Iterable<Lecture>) {
        timetable.forEach { addLecture(it) }
    }

    @Transaction
    suspend fun overwriteTimetable(timetable: Iterable<Lecture>) {
        deleteTimetable()
        addTimetable(timetable)
    }


    // UPDATES
    @Update
    suspend fun updateStudent(student: Student): Int

    @Update
    suspend fun updateProfessor(professor: Professor): Int

    @Update
    suspend fun updateBuilding(building: Building): Int


    // ADD OR UPDATE
    @Transaction
    suspend fun addOrUpdateStudent(student: Student) {
        if (updateStudent(student) == 0) addStudent(student)
    }

    @Transaction
    suspend fun addOrUpdateProfessor(professor: Professor) {
        if (updateProfessor(professor) == 0) addProfessor(professor)
    }

    @Transaction
    suspend fun addOrUpdateBuilding(building: Building) {
        if (updateBuilding(building) == 0) addBuilding(building)
    }

    // DELETE
    @Transaction
    @Query("DELETE FROM student")
    suspend fun deleteStudents()

    @Transaction
    @Query("DELETE FROM lecture")
    suspend fun deleteTimetable()


    //------------------------------------------------------


    // QUERIES

    @Transaction
    @Query("SELECT * FROM lecture ORDER BY start_date")
    fun getTimetable(): Flow<List<Lecture>>

    @Transaction
    @Query("SELECT * FROM lecture WHERE DATE(start_date, 'unixepoch') = DATE('now') ORDER BY start_date")
    fun getTodayTimetable(): Flow<List<Lecture>>

    @Query("SELECT * FROM student LIMIT 1")
    fun getStudentData(): Flow<Student>
}
