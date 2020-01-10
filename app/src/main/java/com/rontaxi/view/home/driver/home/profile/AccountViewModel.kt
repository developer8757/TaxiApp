package com.rontaxi.view.home.driver.home.profile

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.cache.clearAllData
import com.rontaxi.cache.getUser
import com.rontaxi.model.user.User
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.logout.LogoutLiveData
import com.rontaxi.view.home.rider.profile.UpdateProfileLiveData
import com.rontaxi.view.home.rider.profile.UpdateProfileRequestModel

class AccountViewModel(val app: Application): AndroidViewModel(app) {

    fun getUserProfile():User{
        return getUser(app)!!
    }

    lateinit var socketManager: SocketManager

    lateinit var logoutLiveData: LogoutLiveData



    fun logout() {
        logoutLiveData.logout()
    }



    fun disconnectSockets(){
        socketManager.disconnect()
    }

    fun clearData(){
        clearAllData(app)
    }


}