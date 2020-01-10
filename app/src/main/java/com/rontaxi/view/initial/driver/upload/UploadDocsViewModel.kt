package com.rontaxi.view.initial.driver.upload

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.rontaxi.cache.clearAllData
import com.rontaxi.socket.SocketManager
import com.rontaxi.util.getRequestBody
import com.rontaxi.view.home.driver.home.profile.basicinfo.UpdateDriverProfileLiveData
import com.rontaxi.view.home.logout.LogoutLiveData
import com.rontaxi.view.initial.driver.register.Device
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import com.rontaxi.view.initial.driver.register.DriverRegisterLiveData
import com.rontaxi.view.initial.driver.register.DriverRegisterRequest

class UploadDocsViewModel(val app: Application) : AndroidViewModel(app) {


    lateinit var registerLiveData: DriverRegisterLiveData
    lateinit var updateDriverProfileLiveData: UpdateDriverProfileLiveData
    lateinit var logoutLiveData: LogoutLiveData
    lateinit var socketManager: SocketManager


    fun register(driverRegister: DriverRegisterRequest) {

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->

                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result!!.token

                var device = Device()
                device.token = token

                driverRegister.device = getRequestBody(Gson().toJson(device))

                registerLiveData.registerDriver(driverRegister)
            })


    }

    fun updateDriverProfile(updateUserInfoModel: DriverRegisterRequest) {
        updateDriverProfileLiveData.updateDriverProfile(updateUserInfoModel)
    }

    fun saveUser() {
        com.rontaxi.cache.saveUser(
            app,
            updateDriverProfileLiveData.value!!.body()!!.data!!.userObj!!
        )
    }


    fun saveToken() {

        com.rontaxi.cache.saveToken(app, registerLiveData.value!!.body()!!.data!!.loginToken)
    }

    fun logout() {
        logoutLiveData.logout()
    }

    fun disconnectSockets() {
        socketManager.disconnect()
    }

    fun clearData() {
        clearAllData(app)
    }
}