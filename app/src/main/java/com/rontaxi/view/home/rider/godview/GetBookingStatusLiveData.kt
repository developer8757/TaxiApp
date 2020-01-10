package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.constants.GET_BOOKING_STATUS
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.Booking
import com.rontaxi.model.GetBookingStatusResponseModel
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import org.json.JSONArray
import org.json.JSONObject

class GetBookingStatusLiveData(val socketManager: SocketManager) : LiveData<GetBookingStatusResponseModel>(){



    fun getBookingStatus(){

        val json=JSONObject()

        Log.e("BookingStatus","GET_BOOKING_STATUS:  ${JSONObject(Gson().toJson(json))}")

        socketManager.socket!!.emit(GET_BOOKING_STATUS,json,object: Ack{

            override fun call(vararg args: Any?) {

                val pair= isSocketResponseValid(JSONArray(args))
                pair.first?.let {

                    if(isSocketResponseSuccess(it)){

                        socketManager.handler.post {
                            val getBookingStatus= Gson().fromJson<GetBookingStatusResponseModel>(""+it,GetBookingStatusResponseModel::class.java)


                            Log.e("BookingStatus","GET_BOOKING_STATUS:  ${it}")

                            postValue(getBookingStatus)

                        }
                    }

                }

                pair.second?.let {

                    val msg=socketManager.handler.obtainMessage()
                    val bundle=Bundle()
                    bundle.putInt(RESPONSE_CODE_KEY,it.code)
                    bundle.putString(MESSAGE_TO_SHOW_KEY,it.message)
                    msg.data=bundle
                    socketManager.handler.sendMessage(msg)

                }
            }
        })


    }


}