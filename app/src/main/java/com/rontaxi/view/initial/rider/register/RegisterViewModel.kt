package com.rontaxi.view.initial.rider.register

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.rontaxi.util.getRequestBody
import com.rontaxi.view.initial.driver.register.Device

class RegisterViewModel(val app: Application):AndroidViewModel(app)  {


    val TAG="Rider_register"

    lateinit var registerLiveData: RegisterLiveData

    fun register(registerRequestModel: RegisterRequestModel){


        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->

            if(!task.isSuccessful){
                Log.w(TAG, "getInstanceId failed", task.exception)
                return@OnCompleteListener

            }

            val token = task.result!!.token

            var device= Device()
            device.token=token

            registerRequestModel.device= getRequestBody(Gson().toJson(device))


            registerLiveData.registerRider(registerRequestModel)

        })



    }

    fun saveToken(){

        com.rontaxi.cache.saveToken(app,registerLiveData.value!!.body()!!.data!!.loginToken)
    }
}