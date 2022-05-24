package das.losaparecidos.etzi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


/*******************************************************************************
 ****                        Custom Application Class                       ****
 *******************************************************************************/

/*

This class is needed for Hilt Framework.

I also initialize notification channels here so they are not created each time we send a notification.

Avoiding code  repetition

*/

@HiltAndroidApp
class Etzi : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)


        /*------------------------------------------------
        |          Create Notification Channels          |
        ------------------------------------------------*/

        // Create the EHU/UPV Information Notification Channel
        val ehuInformationChannelName = getString(NotificationChannelID.EHU_INFORMATION.nameId)
        val ehuInformationChannelDescription = getString(NotificationChannelID.EHU_INFORMATION.descriptionId)

        val ehuInformationChannel = NotificationChannel(
            NotificationChannelID.EHU_INFORMATION.name,
            ehuInformationChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = ehuInformationChannelDescription
        }


        // Create the Tutorial Reminder Channel
        val provisionalGradesChannelName = getString(NotificationChannelID.PROVISIONAL_GRADES.nameId)
        val provisionalGradesChannelDescription = getString(NotificationChannelID.PROVISIONAL_GRADES.descriptionId)

        val provisionalGradesChannel = NotificationChannel(
            NotificationChannelID.PROVISIONAL_GRADES.name,
            provisionalGradesChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = provisionalGradesChannelDescription
        }


        // Create the Lecture Reminder Channel
        val lectureReminderChannelName = getString(NotificationChannelID.LECTURE_REMINDER.nameId)
        val lectureReminderChannelDescription = getString(NotificationChannelID.LECTURE_REMINDER.descriptionId)

        val lectureReminderChannel = NotificationChannel(
            NotificationChannelID.LECTURE_REMINDER.name,
            lectureReminderChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = lectureReminderChannelDescription
        }


        // Create the Tutorial Reminder Channel
        val tutorialReminderChannelName = getString(NotificationChannelID.TUTORIAL_REMINDER.nameId)
        val tutorialReminderChannelDescription = getString(NotificationChannelID.TUTORIAL_REMINDER.descriptionId)

        val tutorialReminderChannel = NotificationChannel(
            NotificationChannelID.TUTORIAL_REMINDER.name,
            tutorialReminderChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = tutorialReminderChannelDescription
        }


        //-------------------------------------------------------------------

        // Get notification manager
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification group
        val remainderChannelGroupName = getString(NotificationChannelID.REMAINDER_GROUP.nameId)
        val remainderChannelGroup = NotificationChannelGroup(NotificationChannelID.REMAINDER_GROUP.name, remainderChannelGroupName)

        notificationManager.createNotificationChannelGroup(remainderChannelGroup)

        // Add channels to group
        lectureReminderChannel.group = NotificationChannelID.REMAINDER_GROUP.name
        tutorialReminderChannel.group = NotificationChannelID.REMAINDER_GROUP.name

        // Register the channels with the system
        notificationManager.createNotificationChannel(ehuInformationChannel)
        notificationManager.createNotificationChannel(lectureReminderChannel)
        notificationManager.createNotificationChannel(tutorialReminderChannel)
        notificationManager.createNotificationChannel(provisionalGradesChannel)
    }


    // Work Manager Configuration
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}


/*******************************************************************************
 ****                    Enum Class for Notification IDs                    ****
 *******************************************************************************/

/*
Class used to centralize and have better control over application's notification IDs.
It uses an enum class what gives better readability over the code, and avoids ID mistakes

(In this app we only had one notification, but it's a good practice and eases future expansions and technical debt)
*/

enum class NotificationChannelID(val nameId: Int, val descriptionId: Int) {
    EHU_INFORMATION(R.string.ehu_information_channel_name, R.string.ehu_information_channel_description),

    LECTURE_REMINDER(R.string.lecture_reminder_channel_name, R.string.lecture_reminder_channel_description),
    TUTORIAL_REMINDER(R.string.tutorial_reminder_channel_name, R.string.tutorial_reminder_channel_description),
    REMAINDER_GROUP(R.string.reminder_channel_group_name, R.string.reminder_channel_group_description),

    PROVISIONAL_GRADES(R.string.provisional_grades_channel_name, R.string.provisional_grades_channel_description)
}

enum class NotificationID(val id: Int) {
    EHU_NOTIFICATION(1),

    LECTURE_REMINDER(20),
    TUTORIAL_REMINDER(21),

    PROVISIONAL_GRADE(30)
}


//WIDGET PENDING INTENTS
enum class WidgetOpenerActions {
    OPEN_TUTORIALS,
    OPEN_EXPEDIENTE,
    OPEN_EGELA
}