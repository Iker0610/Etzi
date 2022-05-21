package das.losaparecidos.etzi.app.utils

import kotlinx.datetime.*
import java.time.format.DateTimeFormatter


val LocalDate.Companion.today get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
fun LocalDate.format(formatPattern: String): String = this.format(DateTimeFormatter.ofPattern(formatPattern))
fun LocalDate.format(formatter: DateTimeFormatter): String = this.toJavaLocalDate().format(formatter)

val LocalDate.epochSeconds get() = this.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds
val LocalDate.epochUTCMilliseconds get() = this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

fun LocalDate.Companion.fromEpochMilliseconds(millis: Long): LocalDate =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.Companion.fromEpochSeconds(seconds: Long): LocalDate =
    Instant.fromEpochSeconds(seconds).toLocalDateTime(TimeZone.currentSystemDefault()).date

//-------------------------------------------


val LocalDateTime.Companion.now get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun LocalDateTime.format(formatPattern: String): String = this.format(DateTimeFormatter.ofPattern(formatPattern))
fun LocalDateTime.format(formatter: DateTimeFormatter): String = this.toJavaLocalDateTime().format(formatter)

val LocalDateTime.epochSecond get(): Long = this.toInstant(TimeZone.currentSystemDefault()).epochSeconds

fun LocalDateTime.Companion.fromEpochSeconds(seconds: Long): LocalDateTime =
    Instant.fromEpochSeconds(seconds).toLocalDateTime(TimeZone.currentSystemDefault())