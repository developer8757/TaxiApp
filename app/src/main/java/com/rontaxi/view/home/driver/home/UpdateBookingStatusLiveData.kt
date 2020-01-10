package com.rontaxi.view.home.driver.home

import android.arch.lifecycle.LiveData
import android.os.Bundle
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.constants.UPDATE_BOOKING_STATUS
import com.rontaxi.model.GetBookingStatusResponseModel
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import org.json.JSONArray
import org.json.JSONObject

class UpdateBookingStatusLiveData(val socketManager: SocketManager): LiveData<GetBookingStatusResponseModel>() {


    enum class UpdateBookingActions(val value: Int){

        CANCELLED_BY_CUSTOMER(6),
        CANCELLED_BY_DRIVER(7),
        ON_WAY(2),
        COMPLETED(3),
        ARRIVED(4),
        STARTED(5)

    }


    fun updateBookingStatus(bookingId: String, action: UpdateBookingActions, /*only be used in case of rider cancellation*/reasonId: String?){

            val jsonObject=JSONObject()
                jsonObject.put("bookingId", bookingId)
                jsonObject.put("status",action.value)

        if(reasonId!=null){
            jsonObject.put("reasonId",reasonId)
        }




        socketManager.socket!!.emit(UPDATE_BOOKING_STATUS,jsonObject,object: Ack{

            override fun call(vararg args: Any?) {


                val pair= isSocketResponseValid(JSONArray(args))


                pair.first?.let {

                    if(isSocketResponseSuccess(it)){


                        var response=Gson().fromJson<GetBookingStatusResponseModel>(""+it,GetBookingStatusResponseModel::class.java )


                        socketManager.handler.post(Runnable {

                            postValue(response)


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




            }
        })



    }

}