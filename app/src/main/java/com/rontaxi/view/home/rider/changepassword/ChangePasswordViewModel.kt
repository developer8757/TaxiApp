package com.rontaxi.view.home.rider.changepassword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.view.home.logout.LogoutLiveData

class ChangePasswordViewModel(val app: Application) : AndroidViewModel(app) {
    lateinit var changePasswordLiveData: ChangePasswordLiveData
    lateinit var logoutLiveData: LogoutLiveData

    fun changePassword(oldPassword: String, newPassword: String) {
        changePasswordLiveData.changePassword(oldPassword, newPassword)
    }

    fun logout() {
        logoutLiveData.logout()
    }
}