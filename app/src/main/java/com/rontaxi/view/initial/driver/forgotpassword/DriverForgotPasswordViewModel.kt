package com.rontaxi.view.initial.driver.forgotpassword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.model.registration.Phone
import com.rontaxi.view.initial.rider.forgotpassword.ForgotPasswordLiveData
import com.rontaxi.view.initial.rider.forgotpassword.ForgotRequest

class DriverForgotPasswordViewModel(val app: Application): AndroidViewModel(app) {

    lateinit var forgotPasswordLiveData: ForgotPasswordLiveData


    fun forgotPassword(phone: Phone){

        var request=ForgotRequest()
            request.phone=phone

        forgotPasswordLiveData.forgotPassword(request)

    }

    fun saveToken(){
        com.rontaxi.cache.saveToken(app,forgotPasswordLiveData.value!!.body()!!.data!!.loginToken)
    }

}