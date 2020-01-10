package com.rontaxi.view.initial.rider.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.rontaxi.cache.savePhone
import com.rontaxi.model.registration.Phone
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.initial.driver.login.DriverLoginLiveData
import com.rontaxi.view.initial.driver.login.LoginRequestModel

class LoginViewModel(val app: Application): AndroidViewModel(app){

    lateinit var loginLiveData: DriverLoginLiveData

    lateinit var socketManager: SocketManager

    fun login(loginRequestModel: LoginRequestModel){


        socketManager.disconnect()
        var phone= Phone()
            phone=loginRequestModel.phone

        savePhone(app,phone)


        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->

                if(!task.isSuccessful){
                    return@OnCompleteListener
                }
                val token = task.result!!.token

                loginRequestModel.device.token=token

                loginLiveData.login(loginRequestModel)

            })
    }

    fun connectSockets(){
        socketManager.connect()
    }

    fun saveUser(){

        com.rontaxi.cache.saveUser(app,loginLiveData.value!!.body()!!.data!!.userObj!!)
    }

    fun saveToken(){
        com.rontaxi.cache.saveToken(app, loginLiveData.value!!.body()!!.data!!.loginToken)

    }

}