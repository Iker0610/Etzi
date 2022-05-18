package das.losaparecidos.etzi.app.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter


val LocalDate.Companion.today get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
fun LocalDate.format(formatPattern: String): String = this.toJavaLocalDate().format(DateTimeFormatter.ofPattern(formatPattern))

val LocalDate.epochSeconds get() = this.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds
val LocalDate.epochMilliseconds get() = this.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

fun LocalDate.Companion.fromEpochMilliseconds(millis: Long): LocalDate =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.Companion.fromEpochSeconds(seconds: Long): LocalDate =
    Instant.fromEpochSeconds(seconds).toLocalDateTime(TimeZone.currentSystemDefault()).date

//-------------------------------------------


val LocalDateTime.Companion.now get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun LocalDateTime.format(formatPattern: String): String = this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(formatPattern))

val LocalDateTime.epochSecond get(): Long = this.toInstant(TimeZone.currentSystemDefault()).epochSeconds

fun LocalDateTime.Companion.fromEpochMilliseconds(millis: Long): LocalDateTime =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.Companion.fromEpochSeconds(seconds: Long): LocalDateTime =
    Instant.fromEpochSeconds(seconds).toLocalDateTime(TimeZone.currentSystemDefault())