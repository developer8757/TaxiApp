package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.constants.CAR_LISTING_ETA
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.locationupdate.LocationUpdateArrayElement
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.model.nearbycabs.NearByCabsResponseModel
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import org.json.JSONArray
import org.json.JSONObject

class GetNearByCarsLiveData(val  socketManager: SocketManager): LiveData<NearByCabsResponseModel>() {


    fun getNearByCars(currentLocation: LocationUpdateArrayElement){

        val gson=Gson().toJson(currentLocation)

        val json= JSONObject()
            json.put("currentLocation",JSONObject(gson))

        BookingManager.currentBooking?.let {

            if(it.bookingStatus!=0) {

                json.put("bookingId", it.bookingId)
            }
        }


        Log.e("socketAuthenticate","CAR_LISTING_ETA:  ${JSONObject(Gson().toJson(json))}")

        socketManager.socket!!.emit(CAR_LISTING_ETA,json, object: Ack{
            override fun call(vararg args: Any?) {

                val pair= isSocketResponseValid(JSONArray(args))
                pair.first?.let {

                    if(isSocketResponseSuccess(it)){

                     val nearByCabs=   Gson().fromJson(""+it,NearByCabsResponseModel::class.java)

                        socketManager.handler.post(Runnable {


                            Log.w("NearbyCars", ""+it)
                            value=nearByCabs
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