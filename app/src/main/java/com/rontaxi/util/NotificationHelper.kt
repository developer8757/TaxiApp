package com.rontaxi.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat.*
import android.support.v4.content.ContextCompat
import com.rontaxi.R

class NotificationHelper constructor(context: Context) : ContextWrapper(context) {
    private val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    companion object {
        private const val IMPORTENT_NOTIFICATION_TITLE = "Important Notification Channel"
        private const val URGENT_NOTIFICATION_TITLE = " Urgent Notification Channel"
        private const val DEFAULT_NOTIFICATION_TITLE = "Application Notification"
        const val IMPORTENT_NOTIFICATION_CHANNEL = "com.rontaxi.important_channel"
        const val URGENT_NOTIFICATION_CHANNEL = "com.rontaxi.name.comment_channel"
        const val NOTIFICATION_ID = 456
        private const val DEFAULT_NOTIFICATION_CHANNEL = "com.rontaxi.default_channel"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannels()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationChannels = mutableListOf<NotificationChannel>()
        // Building important notification channel

        val messageNotificationChannel = NotificationChannel(
            IMPORTENT_NOTIFICATION_CHANNEL,
            IMPORTENT_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH
        )
        messageNotificationChannel.enableLights(true)
        messageNotificationChannel.lightColor = Color.RED
        messageNotificationChannel.setShowBadge(true)
        messageNotificationChannel.enableVibration(true)
        messageNotificationChannel.setSound(uri, null)
        messageNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationChannels.add(messageNotificationChannel)
        // Building comment notification channel

        val commentNotificationChannel = NotificationChannel(
            URGENT_NOTIFICATION_CHANNEL,
            URGENT_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH
        )
        commentNotificationChannel.enableLights(true)
        commentNotificationChannel.lightColor = Color.RED
        commentNotificationChannel.setShowBadge(true)
        commentNotificationChannel.setSound(uri, null)
        commentNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationChannels.add(commentNotificationChannel)
        // Building default notification channel
        val defaultNotificationChannel = NotificationChannel(
            DEFAULT_NOTIFICATION_CHANNEL,
            DEFAULT_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_DEFAULT
        )
        defaultNotificationChannel.setShowBadge(true)
        defaultNotificationChannel.setSound(uri, null)
        notificationChannels.add(defaultNotificationChannel)
        notificationManager.createNotificationChannels(notificationChannels)
    }

    fun createNotificationBuilder(
        title: String,
        body: String,
        cancelAble: Boolean = true,
        pendingIntent: PendingIntent? = null,
        channelId: String = IMPORTENT_NOTIFICATION_CHANNEL
    ): Notification.Builder {
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Notification.Builder(applicationContext, channelId)
        else
            Notification.Builder(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.app_icon)
            builder.setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE or DEFAULT_LIGHTS)
            builder.setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
        } else
            builder.setSmallIcon(R.drawable.app_icon)
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent)
        builder.setContentTitle(title)
            .setContentText(body)
            .setStyle(Notification.BigTextStyle().bigText(body))
            .setAutoCancel(cancelAble)
        return builder
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun deleteChannel(channelId: String) = apply {
        notificationManager.deleteNotificationChannel(channelId)
    }

    fun makeNotification(builder: Notification.Builder, notificationId: Int = NOTIFICATION_ID): Notification {

        val notification= builder.build()

        apply {
            notificationManager.notify(notificationId, notification)
        }

        return notification
    }



    fun cancelNotification(notificationId: Int) = apply {
        notificationManager.cancel(notificationId)
    }
}