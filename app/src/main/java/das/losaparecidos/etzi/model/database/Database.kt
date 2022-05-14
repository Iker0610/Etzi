package das.losaparecidos.etzi.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import das.losaparecidos.etzi.model.entities.Building
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.Subject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.*
import java.time.chrono.ChronoLocalDate
import java.time.temporal.Temporal

/**
 * Room database definition abstract class (it's later instantiated in Hilt's module).
 *
 * Version: 1
 *
 * Entities: TODO
 * Defined DAOs: TODO
 *
 */

@Database(
    version = 1,
    entities = [Lecture::class, Subject::class, Building::class, Professor::class],
)
@TypeConverters(Converters::class)
abstract class EtziDatabase : RoomDatabase() {
    init {
        TODO("ADD DAOs")
    }
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
    fun fromLongToDate(value: Long): LocalDate =
        Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDate()


    @TypeConverter
    fun fromLongToDatetime(value: Long): LocalDateTime =
        Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime()


    @TypeConverter
    fun dateToTimestamp(date: LocalDate): Long =
        date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()


    @TypeConverter
    fun datetimeToTimestamp(date: LocalDateTime): Long =
        date.atZone(ZoneId.systemDefault()).toEpochSecond()


    //---------   String List Converters   ---------//

    // They convert a list of string to a json and then parse that json back to a list.
    //  - It uses Kotlin serialization library.

    @TypeConverter
    fun listToJson(value: List<String>?): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(value: String): List<String> = Json.decodeFromString(value)
}