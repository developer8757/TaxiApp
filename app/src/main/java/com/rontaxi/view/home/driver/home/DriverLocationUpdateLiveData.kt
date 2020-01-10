package com.rontaxi.view.home.driver.home

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.R
import com.rontaxi.constants.LOCATION_UPDATE
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.locationupdate.LocationUpdateArrayElement
import com.rontaxi.model.locationupdate.LocationUpdateRequestModel
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import com.rontaxi.util.showAlert
import org.json.JSONArray
import org.json.JSONObject

class

DriverLocationUpdateLiveData(val socketManager: SocketManager, val context: Context): LiveData<Boolean>() {

     fun updateLocationToServer(arrayListLocations: ArrayList<LocationUpdateArrayElement>){

        var locationUpdate= LocationUpdateRequestModel()
        locationUpdate.location=arrayListLocations

        var data= Gson().toJson(locationUpdate)

        var json= JSONObject(data)

         BookingManager.currentBooking?.let{

             if(it.bookingStatus==1 || it.bookingStatus==2 || it.bookingStatus==4|| it.bookingStatus==5) {
                 json.put("bookingId", it.bookingId)
             }

         }


        arrayListLocations.clear()

         Log.e("LOCATION_UPDATE","LOCATION_UPDATE:  ${JSONObject(Gson().toJson(json))}")

         socketManager.socket!!.emit(LOCATION_UPDATE,json, object: Ack {

            override fun call(vararg args: Any?) {

                val pair= isSocketResponseValid(JSONArray(args))

                pair.first?.let {

                    if(isSocketResponseSuccess(it)){

                        val msg=socketManager.handler.obtainMessage()
                        val bundle=Bundle()
                            bundle.putString(MESSAGE_TO_SHOW_KEY,""+it)
                        msg.data=bundle

                        socketManager.handler.sendMessage(msg)
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