package com.rontaxi.view.home.driver.home.map

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.R
import com.rontaxi.cache.isDriverOnline
import com.rontaxi.constants.GET_AVAILABILITY_STATUS
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import com.rontaxi.util.showAlert
import org.json.JSONArray
import org.json.JSONObject

class GetAvailabiltyStatusLiveData (val socketManager: SocketManager, val context: Context): LiveData<Boolean>() {

    fun getAvailabiltyStatus(){


        val jsonObject= JSONObject()


        Log.e("socketAuthenticate","GET_AVAILABILITY_STATUS:  ${JSONObject(Gson().toJson(jsonObject))}")

        socketManager.socket!!.emit(GET_AVAILABILITY_STATUS,jsonObject, object: Ack {

            override fun call(vararg args: Any?) {

                val pair= isSocketResponseValid(JSONArray(args))

                pair.first?.let {

                    if(isSocketResponseSuccess(it)){

                        var data= it.getJSONObject("data")

                        isDriverOnline.postValue(data.getBoolean("status"))

                    }else{


                    }

                }

                pair.second?.let {

                    val msg=socketManager.handler.obtainMessage()
                    val bundle= Bundle()
                    bundle.putInt(RESPONSE_CODE_KEY,it.code)
                    bundle.putString(MESSAGE_TO_SHOW_KEY,it.message)
                    msg.data=bundle

                    socketManager.handler.sendMessage(msg)

                }



            }
        })

    }

}