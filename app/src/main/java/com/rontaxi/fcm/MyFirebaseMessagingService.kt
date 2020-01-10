package com.rontaxi.fcm

import android.app.Notification
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build

import android.media.RingtoneManager
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.google.gson.Gson
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.cache.getUser
import com.rontaxi.util.NotificationHelper
import com.rontaxi.util.filterNotifications
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.rider.HomeRiderActivity
import es.dmoral.toasty.Toasty
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        val NOTIFICATION_FLAG = "notification_flag"
        val NOTIFICATION = MutableLiveData<String>()
    }

    //@Inject
    //lateinit var deviceTokanUpdateLivedata: DeviceTokanUpdateLivedata

    override fun onCreate() {
        //  AndroidInjection.inject(this);

        super.onCreate()

    }


    var TAG = "PUSH"
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)


        if (getUser(this) != null) {
            val dataMap = p0?.getData()

            /**
             * filter notifications based on the trip status
             */

            dataMap?.let { filterNotifications(it, this) }
        }

        Log.i(TAG, p0!!.messageId)

    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

        /* p0?.let {

             var updateTokenRequest = DeviceTokenUpdateRequest()
             updateTokenRequest.fcm_token = it
             deviceTokanUpdateLivedata.updateToken(updateTokenRequest)
         }*/

    }



}