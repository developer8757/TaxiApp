package com.rontaxi.view.initial.driver.vehicletype

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class VehicleTypeViewModel(app: Application): AndroidViewModel(app) {


    lateinit var vehicleTypeLiveData: VehicleTypeLiveData
    fun getVehicleType(){

        vehicleTypeLiveData.getCarType()
    }
}