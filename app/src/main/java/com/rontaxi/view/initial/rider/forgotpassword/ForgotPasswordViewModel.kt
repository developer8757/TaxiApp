package com.rontaxi.view.initial.rider.forgotpassword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.model.registration.Phone

class ForgotPasswordViewModel(val app: Application): AndroidViewModel(app) {

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