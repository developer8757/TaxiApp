package com.rontaxi.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rontaxi.BuildConfig
import com.rontaxi.RonTaxiApp
import com.rontaxi.fcm.MyFirebaseMessagingService
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.rider.HomeRiderActivity
import org.json.JSONObject

const val BOOKING_NOTIFICATION = "Booking"


fun filterNotifications(notificationData: Map<String, String>, context: Context) {


    notificationData.let {


        /**
         * filter notifications based on the booking status
         */
        if (BOOKING_NOTIFICATION.equals(it.get("pushType"))) {
            // booking related notifications


            val data = JSONObject(it.get("data"))


            val bookingId = data.get("bookingId").toString()
            val bookingStatus = data.getInt("status")


            var intent: Intent? = null
            var pendingIntent: PendingIntent? = null
            if (BuildConfig.ROLE == 0) {// rider{

                intent = Intent(context, HomeRiderActivity::class.java)

                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                intent.putExtra(
                    MyFirebaseMessagingService.NOTIFICATION_FLAG,
                    MyFirebaseMessagingService.NOTIFICATION_FLAG
                )

                /**
                 * if booking status is 3 that is ride completed then send the bookingId in notification
                 * so that we can get booking details in that activity if not theres
                 */
                if (bookingStatus == 3) {
                    intent.putExtra("bookingId", bookingId)

                }


            } else {
                // driver
                intent = Intent(context, HomeDriverActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

            }


            pendingIntent = PendingIntent.getActivity(
                context, (0..10000).random()/* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )


            val notificationHelper = NotificationHelper(RonTaxiApp.context)
            val builder = notificationHelper.createNotificationBuilder(
                it.get("title")!!,
                it.get("message")!!,
                pendingIntent = pendingIntent

            )
            notificationHelper.makeNotification(builder, 456)

        }
    }
}

fun cancelNotification(context: Context) {

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    //  notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
    notificationManager.cancelAll();


}
