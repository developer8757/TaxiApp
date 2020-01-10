package com.rontaxi.view.home.driver.home.rating

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class DriverRatingViewModel(val app: Application): AndroidViewModel(app) {


    lateinit var driverRatingLiveData: DriverRatingLiveData

    fun getDriverRatings(offset : Int){

        driverRatingLiveData.getDriverRatings(offset)
    }


}