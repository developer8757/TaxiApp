package com.rontaxi.view.initial.rider.resetpassword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel


class ResetPasswordViewModel( val app: Application): AndroidViewModel(app) {

    lateinit var resetPasswordLiveData: ResetPasswordLiveData

    fun resetPassword(password: String) {
        resetPasswordLiveData.resetPassword(password)

    }
}