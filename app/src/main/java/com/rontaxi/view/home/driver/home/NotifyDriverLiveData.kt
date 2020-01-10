package com.rontaxi.view.home.driver.home

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.emitter.Emitter
import com.google.gson.Gson
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.NOTIFY_DRIVER_BOOKING
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.NotifyDriverResponseModel
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import org.json.JSONArray

class NotifyDriverLiveData(val socketManager: SocketManager,val context: Context): LiveData<NotifyDriverResponseModel>(){



    fun startListening(){

        socketManager.socket!!.on(NOTIFY_DRIVER_BOOKING,Emitter.Listener {

            val pair= isSocketResponseValid(JSONArray(it))

            pair.first?.let {

                if(isSocketResponseSuccess(it)){

                    socketManager.handler.post(Runnable {


                        val notifyDriver=Gson().fromJson<NotifyDriverResponseModel>(""+it.getJSONObject("data"),NotifyDriverResponseModel::class.java)

                        postValue(notifyDriver)

                        Log.i("NotifyDriverLiveData",""+it.getJSONObject("data"))


                    })


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
        })

    }



}