package com.rontaxi.view.home.driver.home

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.constants.ACCEPT_REJECT_BOOKING
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.AcceptRejectModel
import com.rontaxi.model.Booking
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import org.json.JSONArray
import org.json.JSONObject

class AcceptRejectLiveData(val socketManager: SocketManager): LiveData<AcceptRejectModel>() {



     enum class AcceptRejectAction(val value: Int){

        ACCEPT(1),
        REJECT(2),


    }



    fun updateBookingStatus(bookingId: String, action: AcceptRejectAction) {



        val jsonObject=JSONObject()
            jsonObject.put("bookingId", bookingId)
            jsonObject.put("status",action.value)


        Log.e("socketAuthenticate","ACCEPT_REJECT_BOOKING:  ${Gson().toJson(jsonObject)}")
        socketManager.socket!!.emit(ACCEPT_REJECT_BOOKING, jsonObject,object: Ack{


            override fun call(vararg args: Any?) {
                val pair= isSocketResponseValid(JSONArray(args))


                pair.first?.let {

                    if(isSocketResponseSuccess(it)){

                        Log.i("DriverResponse", ""+it)


                        val booking= Gson().fromJson<Booking>(""+it.getJSONObject("data").getJSONObject("bookingObj"),Booking::class.java)

                        socketManager.handler.post(Runnable {


                            val model=AcceptRejectModel()
                                model.booking=booking
                                model.action=action

                            postValue(model)
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