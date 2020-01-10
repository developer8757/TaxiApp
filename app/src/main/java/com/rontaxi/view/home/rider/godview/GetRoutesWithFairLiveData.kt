package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.constants.GET_ROUTES_WITH_ETA
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.GetRouteEtaFairModel
import com.rontaxi.model.locationupdate.LocationUpdateArrayElement
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import org.json.JSONArray
import org.json.JSONObject

class GetRoutesWithFairLiveData(val socketManager: SocketManager) :
    LiveData<GetRouteEtaFairModel>() {


    fun getRoutesWithEtaAndPrice(
        selectedCabID: String?,
        pickupLocation: LocationUpdateArrayElement,
        dropOffLocation: LocationUpdateArrayElement
    ) {

        val jsonObj = JSONObject()
        jsonObj.put("carId", selectedCabID)
        val gsonPick = Gson().toJson(pickupLocation)
        val gsonDrop = Gson().toJson(dropOffLocation)
        jsonObj.put("userLocation", JSONObject(gsonPick))
        jsonObj.put("destination", JSONObject(gsonDrop))



        Log.e("socketAuthenticate", "GET_ROUTES_WITH_ETA:  ${JSONObject(Gson().toJson(jsonObj))}")

        socketManager.socket!!.emit(GET_ROUTES_WITH_ETA, jsonObj, object : Ack {
            override fun call(vararg args: Any?) {
                val pair = isSocketResponseValid(JSONArray(args))

                pair.first?.let {

                    if (isSocketResponseSuccess(it)) {

                        socketManager.handler.post(Runnable {

                            val gson = Gson().fromJson<GetRouteEtaFairModel>(
                                "" + it.getJSONObject("data"),
                                GetRouteEtaFairModel::class.java
                            )

                            postValue(gson)
                        })
                    }

                }

                pair.second?.let {

                    val msg = socketManager.handler.obtainMessage()
                    val bundle = Bundle()
                    bundle.putInt(RESPONSE_CODE_KEY, it.code)
                    bundle.putString(MESSAGE_TO_SHOW_KEY, it.message)
                    msg.data = bundle
                    socketManager.handler.sendMessage(msg)

                }


            }
        })


    }


}