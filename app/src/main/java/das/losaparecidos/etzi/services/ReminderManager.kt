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
            launchLectureReminderAction -> {
                intent.getStringExtra("data")?.let { data ->
                    launchLectureReminderNotification(context, Json.decodeFromString(data))
                }
            }
            launchTutorialReminderAction -> {
                intent.getStringExtra("data")?.let { data ->
                    launchTutorialReminderNotification(context, Json.decodeFromString(data))
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
        Log.i("REMINDER", "Reloading reminder alarms.")

        val lectureReminders = runBlocking { return@runBlocking reminderRepository.getAllLectureReminders() }
        lectureReminders.map { lecture -> addLectureReminder(context, lecture) }

        val tutorialReminder = runBlocking { return@runBlocking reminderRepository.getAllTutorialReminders() }
        tutorialReminder.map { tutorial -> addTutorialReminder(context, tutorial) }
    }


    /**
     * Method to launch a reminder notification given a [LectureReminder]
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun launchLectureReminderNotification(context: Context, lectureData: LectureReminder) {
        Log.i("REMINDER", "Launching lecture reminder alarm.")

        // Delete alarm from database
        GlobalScope.launch(Dispatchers.IO) { reminderRepository.removeLectureReminder(lectureData) }

        // Show user created notification
        val builder = NotificationCompat.Builder(context, NotificationChannelID.LECTURE_REMINDER.name)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.lecture_reminder_notification_title, lectureData.lectureDate.format("HH:mm")))
            .setContentText(context.getString(R.string.lecture_reminder_notification_text, lectureData.subject, lectureData.lectureDate.format("HH:mm"), lectureData.lectureRoom))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NotificationID.LECTURE_REMINDER.id, builder.build())
        }
    }


    /**
     * Method to launch a reminder notification given a [TutorialReminder]
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun launchTutorialReminderNotification(context: Context, tutorialData: TutorialReminder) {
        Log.i("REMINDER", "Launching tutorial reminder alarm.")

        // Delete alarm from database
        GlobalScope.launch(Dispatchers.IO) { reminderRepository.removeTutorialReminder(tutorialData) }

        // Show user created notification
        val builder = NotificationCompat.Builder(context, NotificationChannelID.TUTORIAL_REMINDER.name)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.tutorial_reminder_notification_title, tutorialData.tutorialDate.format("HH:mm")))
            .setContentText(context.getString(R.string.tutorial_reminder_notification_text, tutorialData.professorFullName, tutorialData.tutorialDate.format("HH:mm"), tutorialData.lectureRoom))
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

        const val launchLectureReminderAction = "LAUNCH_LECTURE_REMINDER"
        const val launchTutorialReminderAction = "LAUNCH_TUTORIAL_REMINDER"
        const val minutesBeforeReminder = 60

        /*************************************************
         **                   Methods                   **
         *************************************************/

        /**
         * Adds a new alarm to schedule a visit reminder.
         * If the alarm already exists then it gets updated.
         */
        /**
         * Adds a new alarm to schedule a visit reminder.
         * If the alarm already exists then it gets updated.
         */
        fun addLectureReminder(context: Context, lectureReminder: LectureReminder) {
            Log.i("REMINDER", "Adding lecture reminder alarm.")

            // Generate Pending Intent
            val alarmIntent = Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchLectureReminderAction
                intent.putExtra("data", Json.encodeToString(lectureReminder)) // Serialize visit data
                PendingIntent.getBroadcast(context, lectureReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }

            // Get alarm manager and schedule a new alarm
            val systemTZ = TimeZone.currentSystemDefault()

            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                lectureReminder.lectureDate.toInstant(systemTZ).minus(DateTimePeriod(minutes = minutesBeforeReminder), systemTZ).toEpochMilliseconds(),
                alarmIntent
            )
        }


        fun addTutorialReminder(context: Context, tutorialReminder: TutorialReminder) {
            Log.i("REMINDER", "Adding tutorial reminder alarm.")

            // Generate Pending Intent
            val alarmIntent = Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchTutorialReminderAction
                intent.putExtra("data", Json.encodeToString(tutorialReminder)) // Serialize visit data
                PendingIntent.getBroadcast(context, tutorialReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }

            // Get alarm manager and schedule a new alarm
            val systemTZ = TimeZone.currentSystemDefault()

            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                tutorialReminder.tutorialDate.toInstant(systemTZ).minus(DateTimePeriod(minutes = minutesBeforeReminder), systemTZ).toEpochMilliseconds(),
                alarmIntent
            )
        }


        /**
         * Removes an existing scheduled reminder alarm if it exists.
         */
        fun removeLectureReminder(context: Context, lectureReminder: LectureReminder) {
            Log.i("REMINDER", "Trying to remove lecture reminder alarm.")

            // Try to get existing Pending Intent
            Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchLectureReminderAction
                intent.putExtra("data", Json.encodeToString(lectureReminder))
                PendingIntent.getBroadcast(context, lectureReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
            }?.let {

                // If there's an already existing Pending Item delete associated alarm
                Log.i("REMINDER", "Removing lecture reminder alarm.")
                (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(it)
            }
        }

        fun removeTutorialReminder(context: Context, tutorialReminder: TutorialReminder) {
            Log.i("REMINDER", "Trying to remove tutorial reminder alarm.")

            // Try to get existing Pending Intent
            Intent(context, ReminderManager::class.java).let { intent ->
                intent.action = launchTutorialReminderAction
                intent.putExtra("data", Json.encodeToString(tutorialReminder))
                PendingIntent.getBroadcast(context, tutorialReminder.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
            }?.let {

                // If there's an already existing Pending Item delete associated alarm
                Log.i("REMINDER", "Removing tutorial reminder alarm.")
                (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(it)
            }
        }
    }
}


/*******************************************************************************
 ****                                Utility                                ****
 *******************************************************************************/

enum class ReminderStatus(val icon: ImageVector) {
    UNAVAILABLE(Icons.Filled.NotificationsOff),
    ON(Icons.Filled.NotificationsActive),
    OFF(Icons.Filled.NotificationsNone)
}