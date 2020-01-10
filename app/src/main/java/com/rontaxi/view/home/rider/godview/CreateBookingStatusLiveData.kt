package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import android.content.Context
import com.github.nkzawa.emitter.Emitter
import com.google.gson.Gson
import com.rontaxi.constants.CREATE_BOOKING_LISTENER
import com.rontaxi.model.CreateBookingStatusResponseModel
import com.rontaxi.socket.SocketManager
import es.dmoral.toasty.Toasty
import org.json.JSONArray
import org.json.JSONObject

class CreateBookingStatusLiveData(val socketManager: SocketManager, val context: Context): LiveData<CreateBookingStatusResponseModel>(){


    fun startListeningCreateBookingStatus(){

        socketManager.socket!!.on(CREATE_BOOKING_LISTENER,Emitter.Listener {


            val jsonArray=JSONArray(it)
            val jsonObject=jsonArray[0] as JSONObject
            val response=Gson().fromJson<CreateBookingStatusResponseModel>(""+jsonObject,CreateBookingStatusResponseModel::class.java)


            socketManager.handler.post {
                postValue(response)
            }


        })


    }



}