package das.losaparecidos.etzi.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import das.losaparecidos.etzi.app.utils.epochSecond
import das.losaparecidos.etzi.app.utils.epochSeconds
import das.losaparecidos.etzi.app.utils.fromEpochSeconds
import das.losaparecidos.etzi.model.database.daos.ReminderDao
import das.losaparecidos.etzi.model.database.daos.StudentCacheDataDao
import das.losaparecidos.etzi.model.entities.Building
import das.losaparecidos.etzi.model.entities.LectureEntity
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.Student
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room database definition abstract class (it's later instantiated in Hilt's module).
 *
 * Version: 2
 *
 * Entities: [LectureEntity], [Building], [Professor], [Student]
 * Defined DAOs: [StudentCacheDataDao], [ReminderDao]
 *
 */

@Database(
    version = 2,
    entities = [LectureEntity::class, Building::class, Professor::class, Student::class],
)
@TypeConverters(Converters::class)
abstract class EtziDatabase : RoomDatabase() {
    abstract fun timetableDao(): StudentCacheDataDao
    abstract fun reminderDao(): ReminderDao
}


//-----------------------------------------------------------------------------------------------------


/**
 * Type converter for ROOM database
 *
 * These methods convert the given type to a compatible type that SQLite supports and vice versa.
 * ROOM database automatically knows which type converters must use.
 */

class Converters {
    //--------   ZonedDateTime Converters   --------//

    // They convert from ZonedDateTime to long format and backwards. Time zone value is kept.

    @TypeConverter
    fun fromLongToDate(value: Long): LocalDate = LocalDate.fromEpochSeconds(value)


    @TypeConverter
    fun fromLongToDatetime(value: Long): LocalDateTime = LocalDateTime.fromEpochSeconds(value)


    @TypeConverter
    fun dateToTimestamp(date: LocalDate): Long = date.epochSeconds


    @TypeConverter
    fun datetimeToTimestamp(date: LocalDateTime): Long = date.epochSecond


    //---------   String List Converters   ---------//

    // They convert a list of string to a json and then parse that json back to a list.
    //  - It uses Kotlin serialization library.

    @TypeConverter
    fun listToJson(value: List<String>?): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(value: String): List<String> = Json.decodeFromString(value)
}