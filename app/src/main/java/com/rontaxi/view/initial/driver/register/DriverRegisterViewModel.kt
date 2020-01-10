package com.rontaxi.view.initial.driver.register

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class DriverRegisterViewModel(app: Application): AndroidViewModel(app) {


    lateinit var validatePhoneLiveData: ValidatePhoneLiveData

    fun validatePhone(request: PhoneRequestModel){
        validatePhoneLiveData.validatePhone(request)
    }
}