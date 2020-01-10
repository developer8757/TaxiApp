package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.socketio.client.Ack
import com.google.gson.Gson
import com.rontaxi.constants.CREATE_BOOKING
import com.rontaxi.constants.MESSAGE_TO_SHOW_KEY
import com.rontaxi.constants.RESPONSE_CODE_KEY
import com.rontaxi.model.Booking
import com.rontaxi.model.CreateBookingRequestModel
import com.rontaxi.model.map.Address
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.socket.SocketManager
import com.rontaxi.socket.isSocketResponseSuccess
import com.rontaxi.socket.isSocketResponseValid
import com.rontaxi.util.ProgressDialog
import org.json.JSONArray
import org.json.JSONObject

class CreateBookingLiveData(val socketManager: SocketManager): LiveData<Booking>() {


    fun createBooking(selectedCab: CabsDetails, pickUpAddress: Address, dropOffAddress: Address, tentativePrice: String){

            val createBookingModel=CreateBookingRequestModel()
                createBookingModel.startingPoint.lat=pickUpAddress.lat
                createBookingModel.startingPoint.log=pickUpAddress.lng
                createBookingModel.startingPoint.bearing=0f

                createBookingModel.endingPoint.lat=dropOffAddress.lat
                createBookingModel.endingPoint.log=dropOffAddress.lng
                createBookingModel.endingPoint.bearing=0f

                createBookingModel.carId=selectedCab.carId


                createBookingModel.pickupAddress=pickUpAddress.address!!

                createBookingModel.dropAddress=dropOffAddress.address!!

                createBookingModel.tentativeBudget=tentativePrice




        Log.e("socketAuthenticate","CREATE_BOOKING:  ${JSONObject(Gson().toJson(createBookingModel))}")
            socketManager.socket!!.emit(CREATE_BOOKING,JSONObject(Gson().toJson(createBookingModel)),

                object: Ack{

                    override fun call(vararg args: Any?) {



                        val pair= isSocketResponseValid(JSONArray(args))
                        pair.first?.let {

                            if (isSocketResponseSuccess(it)) {

                                Log.w("CREATE_BOOKING", ""+it)


                                val bookingResponse=   Gson().fromJson(""+it.getJSONObject("data").getJSONObject("bookingObj"),Booking::class.java)

                                socketManager.handler.post(Runnable {
                                    postValue(bookingResponse)

                                })


                            }
                        }


                        pair.second?.let {

                            ProgressDialog.hideProgressBar()

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