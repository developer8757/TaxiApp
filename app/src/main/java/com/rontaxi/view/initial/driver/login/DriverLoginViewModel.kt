package com.rontaxi.view.initial.driver.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.rontaxi.socket.SocketManager

class DriverLoginViewModel(val app: Application) : AndroidViewModel(app) {


    lateinit var loginLiveData: DriverLoginLiveData
    lateinit var socketManager: SocketManager
    var TAG = "LOGIN_VIEW_MODEL"

    fun login(loginRequestModel: LoginRequestModel) {

        FirebaseInstanceId.getInstance()
            .instanceId.addOnCompleteListener(OnCompleteListener { task ->

            if (!task.isSuccessful) {
                Log.w(TAG, "getInstanceId failed", task.exception)
                return@OnCompleteListener

            }

            val token = task.result!!.token

            loginRequestModel.device.token = token

            var json = Gson().toJson(loginRequestModel)

            loginLiveData.login(loginRequestModel)

        })


    }

    fun connectSockets() {
        socketManager.connect()
    }


    fun saveUser() {

        com.rontaxi.cache.saveUser(app, loginLiveData.value!!.body()!!.data!!.userObj!!)
    }

    fun saveToken() {
        com.rontaxi.cache.saveToken(app, loginLiveData.value!!.body()!!.data!!.loginToken)

    }


}