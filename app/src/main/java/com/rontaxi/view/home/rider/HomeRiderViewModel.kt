package com.rontaxi.view.home.rider

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.cache.clearAllData
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.logout.LogoutLiveData


class HomeRiderViewModel(val app: Application) : AndroidViewModel(app){

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