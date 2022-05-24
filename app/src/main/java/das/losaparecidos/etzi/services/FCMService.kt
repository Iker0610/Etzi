package das.losaparecidos.etzi.services

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import das.losaparecidos.etzi.NotificationChannelID
import das.losaparecidos.etzi.NotificationID
import das.losaparecidos.etzi.R


/**
 * Service for handling FCM messages when application is in foreground.
 */
class FCMService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {}


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { notification ->
            Log.d("FCM", "Message Notification Title: ${notification.title}")
            Log.d("FCM", "Message Notification Body: ${notification.body}")

            if (notification.channelId == NotificationChannelID.PROVISIONAL_GRADES.name && notification.titleLocalizationKey != null && notification.bodyLocalizationKey != null) {
                // Show user created notification
                val title = resources.getString(resources.getIdentifier(notification.titleLocalizationKey, "string", packageName))
                val text = resources.getString(resources.getIdentifier(notification.bodyLocalizationKey, "string", packageName), *notification.bodyLocalizationArgs)

                val builder = NotificationCompat.Builder(this, notification.channelId!!)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)

                with(NotificationManagerCompat.from(this)) {
                    notify(NotificationID.PROVISIONAL_GRADE.id, builder.build())
                }

            } else {
                // Show user created notification
                val builder = NotificationCompat.Builder(this, NotificationChannelID.EHU_INFORMATION.name)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(notification.title)
                    .setContentText(notification.body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)

                with(NotificationManagerCompat.from(this)) {
                    notify(NotificationID.EHU_NOTIFICATION.id, builder.build())
                }
            }
        }
    }
}