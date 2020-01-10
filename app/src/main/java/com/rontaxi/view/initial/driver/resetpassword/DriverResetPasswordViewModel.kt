package com.rontaxi.view.initial.driver.resetpassword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.view.initial.rider.resetpassword.ResetPasswordLiveData

class DriverResetPasswordViewModel(app: Application): AndroidViewModel(app) {


    lateinit var resetPasswordLiveData: ResetPasswordLiveData

    fun resetPassword(password: String){
        resetPasswordLiveData.resetPassword(password)

    }



}