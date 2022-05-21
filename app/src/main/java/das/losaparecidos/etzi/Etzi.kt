package das.losaparecidos.etzi

import android.app.Application
import android.app.NotificationChannel
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


        // Get notification manager
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        // Register the channels with the system
        notificationManager.createNotificationChannel(ehuInformationChannel)
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
    EHU_INFORMATION(R.string.ehu_information_channel_name, R.string.ehu_information_channel_description)
}

enum class NotificationID(val id: Int) {
    USER_CREATED(0),
    CORPORATION_NOTIFICATION(1),
    VISIT_ALARM(2)
}