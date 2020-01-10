package com.rontaxi.view.home.rider.ridehistory

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.view.home.rider.help.HelpReasonsLiveData
import com.rontaxi.view.home.rider.payment.BlockDriverLiveData

class RideHistoryDetailViewModel(app:Application):AndroidViewModel(app) {

    lateinit var helpReasonsLiveData: HelpReasonsLiveData
    lateinit var blockDriverLiveData: BlockDriverLiveData

    fun getHelpReasons(type: Int) {
        helpReasonsLiveData.getHelpPoints(type)
    }

    fun getBlockDriverReasons(data:BlockDriverModel)
    {
        blockDriverLiveData.blockDriver(data)
    }
}