package das.losaparecidos.etzi.services


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.NotificationChannelID
import das.losaparecidos.etzi.NotificationID
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.model.entities.LectureReminder
import das.losaparecidos.etzi.model.entities.TutorialReminder
import das.losaparecidos.etzi.model.repositories.ReminderRepository
import kotlinx.coroutines.*
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@AndroidEntryPoint
class ReminderManager : BroadcastReceiver() {
    /*************************************************
     **                  Attributes                 **
     *************************************************/

    @Inject
    lateinit var reminderRepository: ReminderRepository


    /*************************************************
     **           Broadcast Event Handler           **
     *************************************************/

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "android.intent.action.BOOT_COMPLETED" -> reloadAlarms(context)
            launchLectureRemainderAction -> {
                intent.getStringExtra("data")?.let { data ->
                    launchLectureRemainderNotification(context, Json.decodeFromString(data))
                }
            }
            launchTutorialRemainderAction -> {
                intent.getStringExtra("data")?.let { data ->
                    launchTutorialRemainderNotification(context, Json.decodeFromString(data))
                }
            }
        }
    }


    /*************************************************
     **                    Events                   **
     *************************************************/

    /**
     * Method to reload all alarms.
     */
    private fun reloadAlarms(context: Context) {
        Log.i("REMAINDER", "Reloading remainder alarms.")

        val lectureRemainders = runBlocking { return@runBlocking reminderRepository.getAllLectureRemainders() }
        lectureRemainders.map { lecture -> addLectureRemainder(context, lecture) }

        val tutorialReminder = runBlocking { return@runBlocking reminderRepository.getAllTutorialRemainders() }
        tutorialReminder.map { tutorial -> addTutorialRemainder(context, tutorial) }
    }


    /**
     * Method to launch a remainder notification given a [LectureReminder]
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun launchLectureRemainderNotification(context: Context, lectureData: LectureReminder) {
        Log.i("REMAINDER", "Launching lecture remainder alarm.")

        // Delete alarm from database
        GlobalScope.launch(Dispatchers.IO) { reminderRepository.removeLectureReminder(lectureData) }

        // Show user created notification
        val builder = NotificationCompat.Builder(context, NotificationChannelID.LECTURE_REMINDER.name)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.lecture_remainder_notification_title, lectureData.lectureDate.format("HH:mm")))
            .setContentText(context.getString(R.string.lecture_remainder_notification_text, lectureData.subject, lectureData.lectureDate.format("HH:mm"), lectureData.lectureRoom))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NotificationID.LECTURE_REMINDER.id, builder.build())
        }
    }


    /**
     * Method to launch a remainder notification given a [TutorialReminder]
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun launchTutorialRemainderNotification(context: Context, tutorialData: TutorialReminder) {
        Log.i("REMAINDER", "Launching tutorial remainder alarm.")

        // Delete alarm from database
        GlobalScope.launch(Dispatchers.IO) { reminderRepository.removeTutorialReminder(tutorialData) }

        // Show user created notification
        val builder = NotificationCompat.Builder(context, NotificationChannelID.TUTORIAL_REMINDER.name)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.tutorial_remainder_notification_title, tutorialData.tutorialDate.format("HH:mm")))
            .setContentText(context.getString(R.string.tutorial_remainder_notification_text, tutorialData.professorFullName, tutorialData.tutorialDate.format("HH:mm"), tutorialData.lectureRoom))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NotificationID.LECTURE_REMINDER.id, builder.build())
        }
    }


    /*******************************************************************************
     ****                       Utilities to manage alarms                      ****
     *******************************************************************************/

    companion object {

        /*************************************************
         **                  Constants                  **
         *************************************************/

        const val launchLectureRemainderAction = "LAUNCH_LECTURE_REMAINDER"
        const val launchTutorialRemainderAction = "LAUNCH_TUTORIAL_REMAINDER"
        const val minutesBeforeRemainder = 60

        /*************************************************
         **                   Methods                   **
         *************************************************/

        /**
         * Adds a new alarm to schedule a visit remainder.
         * If the alarm already exists then it gets updated.
         */
        /**
         * Adds a new alarm to schedule a visit remainder.
         * If the alarm already exists then it gets updated.
         */
        fun addLectureRemainder(context: Context, lectureReminder: LectureReminder) {
            Log.i("REMAINDER", "Adding lecture remainder alarm.")

            // Generate Pending Intent
            val alarmIntent = Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchLectureRemainderAction
                intent.putExtra("data", Json.encodeToString(lectureReminder)) // Serialize visit data
                PendingIntent.getBroadcast(context, lectureReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }

            // Get alarm manager and schedule a new alarm
            val systemTZ = TimeZone.currentSystemDefault()

            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                lectureReminder.lectureDate.toInstant(systemTZ).minus(DateTimePeriod(minutes = minutesBeforeRemainder), systemTZ).toEpochMilliseconds(),
                alarmIntent
            )
        }


        fun addTutorialRemainder(context: Context, tutorialReminder: TutorialReminder) {
            Log.i("REMAINDER", "Adding tutorial remainder alarm.")

            // Generate Pending Intent
            val alarmIntent = Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchTutorialRemainderAction
                intent.putExtra("data", Json.encodeToString(tutorialReminder)) // Serialize visit data
                PendingIntent.getBroadcast(context, tutorialReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }

            // Get alarm manager and schedule a new alarm
            val systemTZ = TimeZone.currentSystemDefault()

            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                tutorialReminder.tutorialDate.toInstant(systemTZ).minus(DateTimePeriod(minutes = minutesBeforeRemainder), systemTZ).toEpochMilliseconds(),
                alarmIntent
            )
        }


        /**
         * Removes an existing scheduled remainder alarm if it exists.
         */
        fun removeLectureRemainder(context: Context, lectureReminder: LectureReminder) {
            Log.i("REMAINDER", "Trying to remove lecture remainder alarm.")

            // Try to get existing Pending Intent
            Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchLectureRemainderAction
                intent.putExtra("data", Json.encodeToString(lectureReminder))
                PendingIntent.getBroadcast(context, lectureReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
            }?.let {

                // If there's an already existing Pending Item delete associated alarm
                Log.i("REMAINDER", "Removing lecture remainder alarm.")
                (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(it)
            }
        }

        fun removeTutorialRemainder(context: Context, tutorialReminder: TutorialReminder) {
            Log.i("REMAINDER", "Trying to remove tutorial remainder alarm.")

            // Try to get existing Pending Intent
            Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchTutorialRemainderAction
                intent.putExtra("data", Json.encodeToString(tutorialReminder))
                PendingIntent.getBroadcast(context, tutorialReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
            }?.let {

                // If there's an already existing Pending Item delete associated alarm
                Log.i("REMAINDER", "Removing tutorial remainder alarm.")
                (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(it)
            }
        }
    }
}


/*******************************************************************************
 ****                                Utility                                ****
 *******************************************************************************/

enum class RemainderStatus(val icon: ImageVector) {
    UNAVAILABLE(Icons.Filled.NotificationsOff),
    ON(Icons.Filled.NotificationsActive),
    OFF(Icons.Filled.NotificationsNone)
}