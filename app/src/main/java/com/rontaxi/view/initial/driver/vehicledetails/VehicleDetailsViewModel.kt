package com.rontaxi.view.initial.driver.vehicledetails

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.view.initial.driver.upload.ValidateDocsLiveData

class VehicleDetailsViewModel(app: Application): AndroidViewModel(app) {

    lateinit var validateDocsLiveData: ValidateDocsLiveData

    fun validateVehiclePlate(vehiclePlate: String, phoneNumber: String?){

        validateDocsLiveData.validDocs(vehiclePlate,1, phoneNumber)
    }

}