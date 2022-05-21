package das.losaparecidos.etzi.services


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.ui.graphics.vector.ImageVector

//
///**
// * Class that manages broadcast messages and is in charge of:
// * - Launch remainder notifications.
// * - Reload all notifications after a device reset.
// *
// * It also defines utility methods for creating / editing and deleting remainder alarms.
// */
//@AndroidEntryPoint
//class RemainderManager : BroadcastReceiver() {
//
//    /*************************************************
//     **                  Attributes                 **
//     *************************************************/
//
//    @Inject
//    lateinit var remainderRepository: RemainderRepository
//
//
//    /*************************************************
//     **           Broadcast Event Handler           **
//     *************************************************/
//
//    override fun onReceive(context: Context, intent: Intent) {
//        when (intent.action) {
//            "android.intent.action.BOOT_COMPLETED" -> reloadAlarms(context)
//            launchRemainderAction -> {
//                Log.i("REMAINDER", "Received launch broadcast action.")
//                intent.getStringExtra("data")?.let { data ->
//                    launchVisitRemainderNotification(context, Json.decodeFromString(data))
//                }
//            }
//        }
//    }
//
//
//    /*************************************************
//     **                    Events                   **
//     *************************************************/
//
//    /**
//     * Method to launch a remainder notification given a [CompactVisitData]
//     */
//    @OptIn(DelicateCoroutinesApi::class)
//    private fun launchVisitRemainderNotification(context: Context, visit: CompactVisitData) {
//        Log.i("REMAINDER", "Launching remainder alarm.")
//
//        // Delete alarm from database
//        GlobalScope.launch(Dispatchers.IO) { remainderRepository.removeRemainder(visit.id) }
//
//        // Show user created notification
//        val builder = NotificationCompat.Builder(context, NotificationChannelID.AUTH_CHANNEL.name)
//            .setSmallIcon(R.drawable.ic_stat_name)
//            .setContentTitle(context.getString(R.string.visit_remainder_notification_title, visit.hour))
//            .setContentText(context.getString(R.string.visit_remainder_notification_text, visit.hour, visit.client))
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//
//        with(NotificationManagerCompat.from(context)) {
//            notify(NotificationID.VISIT_ALARM.id, builder.build())
//        }
//    }
//
//    /**
//     * Method to reload all alarms.
//     */
//    private fun reloadAlarms(context: Context) {
//        Log.i("REMAINDER", "Reloading remainder alarms.")
//
//        val visitsWithAlarm = runBlocking { return@runBlocking remainderRepository.getVisitCardsWithRemainders().first() }
//        visitsWithAlarm.map { addVisitRemainder(context, it) }
//    }
//
//
//    /*******************************************************************************
//     ****                       Utilities to manage alarms                      ****
//     *******************************************************************************/
//
//    companion object {
//        /*************************************************
//         **                  Constants                  **
//         *************************************************/
//
//        const val launchRemainderAction = "LAUNCH_VISIT_REMAINDER"
//        const val minutesBeforeVisit = 15L
//
//
//        /*************************************************
//         **                   Methods                   **
//         *************************************************/
//
//        /**
//         * Adds a new alarm to schedule a visit remainder.
//         * If the alarm already exists then it gets updated.
//         */
//        fun addVisitRemainder(context: Context, visitCard: VisitCard) {
//            Log.i("REMAINDER", "Adding remainder alarm.")
//
//            // Map VisitCard to a reduced version
//            val reducedVisitData = CompactVisitData(visitCard)
//
//            // Generate Pending Intent
//            val alarmIntent = Intent(context, VisitRemainder::class.java).let { intent ->
//                intent.action = launchRemainderAction
//                intent.putExtra("data", Json.encodeToString(reducedVisitData)) // Serialize visit data
//                PendingIntent.getBroadcast(context, visitCard.intId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//            }
//
//            // Get alarm manager and schedule a new alarm
//            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                visitCard.visitDate.minusMinutes(minutesBeforeVisit).withSecond(0).toInstant().toEpochMilli(),
//                alarmIntent
//            )
//        }
//
//        /**
//         * Removes an existing scheduled remainder alarm if it exists.
//         */
//        fun removeVisitRemainder(context: Context, visitCard: VisitCard) {
//            Log.i("REMAINDER", "Trying to remove remainder alarm.")
//
//            // Map VisitCard to a reduced version
//            val reducedVisitData = CompactVisitData(visitCard)
//
//            // Try to get existing Pending Intent
//            Intent(context, VisitRemainder::class.java).let { intent ->
//                intent.action = launchRemainderAction
//                intent.putExtra("data", Json.encodeToString(reducedVisitData))
//                PendingIntent.getBroadcast(context, visitCard.intId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE)
//            }?.let {
//
//                // If there's an already existing Pending Item delete associated alarm
//                Log.i("REMAINDER", "Removing remainder alarm.")
//                (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(it)
//            }
//        }
//    }
//}


/*******************************************************************************
 ****                                Utility                                ****
 *******************************************************************************/

enum class RemainderStatus(val icon: ImageVector) {
    UNAVAILABLE(Icons.Filled.NotificationsOff),
    ON(Icons.Filled.NotificationsActive),
    OFF(Icons.Filled.NotificationsNone)
}