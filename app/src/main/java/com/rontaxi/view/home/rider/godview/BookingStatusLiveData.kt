package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.emitter.Emitter
import com.google.gson.Gson
import com.rontaxi.constants.BOOKING_STATUS
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.Booking
import com.rontaxi.model.GetBookingStatusResponseModel
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseValid
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import org.json.JSONArray

class BookingStatusLiveData(val socketManager: SocketManager): LiveData<GetBookingStatusResponseModel>() {


    fun startListeningBookingStatus(){

        socketManager.socket!!.on(BOOKING_STATUS,Emitter.Listener {

            val pair= isSocketResponseValid(JSONArray(it))


            pair.first?.let {

                val response=Gson().fromJson<GetBookingStatusResponseModel>(""+it,GetBookingStatusResponseModel::class.java)

                Log.i("BookingStatusLiveData",""+it)

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

        })

    }

}