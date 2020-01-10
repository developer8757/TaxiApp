package com.rontaxi.location

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.rontaxi.R
import com.rontaxi.RonTaxiApp
import com.rontaxi.cache.getDriverSavedOnlineStatus
import com.rontaxi.util.NotificationHelper
import com.rontaxi.view.home.driver.HomeDriverActivity


class LocationUpdateService: Service() {


    val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }




    var notification: Notification?=null


    val notificationHelper: NotificationHelper by lazy {

        NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var message=""

        if(getDriverSavedOnlineStatus(this)){
            message=getString(R.string.driver_is_online)
        }else{
            message=getString(R.string.updating_location)
        }
        val notificationIntent = Intent(this, HomeDriverActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )



        val builder = notificationHelper.createNotificationBuilder(
            getString(R.string.app_name),
            message,
            pendingIntent = pendingIntent

        )
        notification=notificationHelper.makeNotification(builder, 777)


        startForeground(777, builder.build())

        //do heavy work on a background thread


        //stopSelf();

        return Service.START_NOT_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }



    override fun onDestroy() {

        notificationHelper.cancelNotification(777)

        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }


}