package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.constants.GET_SOS_NUMBER
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.SOSResponseModel
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseValid
import org.json.JSONArray
import org.json.JSONObject

class GetSOSLiveData(val socketManager: SocketManager) : LiveData<SOSResponseModel>(){

    fun getSOS(){


        var json= JSONObject()

        socketManager.socket!!.emit(GET_SOS_NUMBER, json,object: Ack{


            override fun call(vararg args: Any?) {


                val pair= isSocketResponseValid(JSONArray(args))


                pair.first?.let {


                         val response= Gson().fromJson<SOSResponseModel>(""+it, SOSResponseModel::class.java)

                    socketManager.handler.post(Runnable {

                         postValue(response)
                    })

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
        }
        )

    }



}