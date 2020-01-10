package com.rontaxi.view.home.driver.home.earnings

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class DriverEarningViewModel( app: Application): AndroidViewModel(app) {
    lateinit var driverEarningLiveData:DriverEarningLiveData
    fun driverEarning(type:Int,offSet:Int){
        driverEarningLiveData.driverEarning(type,offSet)
    }


}