package com.rontaxi.view.home.rider.ridehistory

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class RideHistoryViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var rideHistoryLiveData: RideHistoryLiveData

    fun rideHistory(type: Int, offSet: Int) {
        rideHistoryLiveData.rideHistory(type, offSet)
    }

}