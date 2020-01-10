package com.rontaxi.socket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.rontaxi.BuildConfig
import com.rontaxi.cache.clearAllData
import com.rontaxi.cache.currentSocketState
import com.rontaxi.cache.getToken
import com.rontaxi.constants.AUTHENTICATE
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.rider.InitialRiderActivity
import es.dmoral.toasty.Toasty
import org.json.JSONArray
import org.json.JSONObject


class SocketManager(val socket: Socket?, val context: Context) {


    val TAG = SocketManager::class.java.canonicalName


    init {
        connect()
    }


    fun authenticate() {

        if (socket!!.connected()) {

            val jsonObject = JSONObject()
            jsonObject.put("token", "Bearer ${getToken(context)}")

            Log.e("socketAuthenticate", "check:  ${Gson().toJson(jsonObject)}")
            socket.emit(AUTHENTICATE, jsonObject, object : Ack {
                override fun call(vararg args: Any?) {

                    val pair = isSocketResponseValid(JSONArray(args))

                    pair.first?.let {

                        if (isSocketResponseSuccess(it)) {
                            Log.e("socketAuthenticate", "SOCKET_STATE.AUTHENTICATED")
                            currentSocketState.postValue(SOCKET_STATE.AUTHENTICATED)
                        }
                    }

                    pair.second?.let {

                        val msg = handler.obtainMessage()
                        val bundle = Bundle()
                        bundle.putInt(RESPONSE_CODE_KEY, it.code)
                        bundle.putString(MESSAGE_TO_SHOW_KEY, it.message)
                        msg.data = bundle

                        handler.sendMessage(msg)

                        // handler.sendEmptyMessage(it.code)

                    }


                }
            })
        } else {
            connect()
        }

    }


    var handler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            /* when(msg?.what){

                 401->{



                     Toasty.error(context!!,"Something went wrong").show()


                 }
             }*/


            try {
                val bundle = msg!!.data


                Log.e("SocketManager", " $bundle")

                val message = bundle.getString(MESSAGE_TO_SHOW_KEY)
                val code = bundle.getInt(RESPONSE_CODE_KEY)

                when (code) {

                    // unAuthorize
                    401 -> {

                        Log.e("socketAuthenticate", "Logout 401 in socket")

                        clearAllData(context)

                        disconnect()


                        if (BuildConfig.ROLE == 1) {

                            socket!!.disconnect()
                            val intent = Intent(context, InitialDriverActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        } else {
                            socket!!.disconnect()
                            val intent = Intent(context, InitialRiderActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }

                    }

                    400->{

                        Toasty.error(context,message).show()

                    }

                }



                // Toasty.info(context!!, message).show()
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
            }

        }

    }

    //Connect to the server and start listening the events from server
    fun connect() {

        if (!getToken(context).isNullOrEmpty()) {

            socket?.on("connect") {
                Log.e("socketAuthenticate", "SOCKET_STATE.CONNECTED")
                currentSocketState.postValue(SOCKET_STATE.CONNECTED)
                authenticate()

            }?.on("disconnect") {
                Log.e("socketAuthenticate", "SOCKET_STATE.DISCONNECTED")
                currentSocketState.postValue(SOCKET_STATE.DISCONNECTED)
            }

            socket?.connect()

        } else {
            Log.e("socketAuthenticate", "No Access Token")
        }
    }


    /*
    disconnect from server and stop listening to the events
     */

    fun disconnect() {

        socket?.disconnect()
        //socket?.off("connect",onConnect)


    }



    enum class SOCKET_STATE {
        DISCONNECTED,
        CONNECTED,
        AUTHENTICATED

    }


}