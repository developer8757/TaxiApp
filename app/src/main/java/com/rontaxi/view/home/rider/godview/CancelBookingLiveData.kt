package com.rontaxi.view.home.rider.godview

import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.socket.SocketManager
import org.json.JSONObject

class CancelBookingLiveData(val socketManager: SocketManager) {

    fun cancelBooking(bookingId: String){

        val json=JSONObject()
            json.put("bookingId",bookingId)


        Log.e("socketAuthenticate","cancelBooking:  ${Gson().toJson(json)}")
        socketManager.socket!!.emit("cancelBooking",json, object: Ack{

            override fun call(vararg args: Any?) {


            }
        })


    }

}