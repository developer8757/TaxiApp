package com.rontaxi.view.initial.driver.license

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.view.initial.driver.upload.ValidateDocsLiveData

class LicenseDetailsViewModel(app: Application): AndroidViewModel(app) {

    lateinit var validateDocsLiveData: ValidateDocsLiveData


    fun validateLicense(licenseNumber: String,phoneNumber:String?){

        validateDocsLiveData.validDocs(licenseNumber,0,phoneNumber)
    }

}