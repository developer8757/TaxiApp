package com.rontaxi.view.home.driver.home.profile.updatephone

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.cache.getUser
import com.rontaxi.model.registration.Phone
import com.rontaxi.model.user.User
import com.rontaxi.view.initial.driver.register.PhoneRequestModel

class ChangePhoneViewModel(val app: Application): AndroidViewModel(app) {

    lateinit var UpdateDriverPhoneLiveData : ChangePhoneLiveData

    fun changePhone(phone: Phone){

        val request= PhoneRequestModel()
        request.phone=phone

        UpdateDriverPhoneLiveData.changePhone(request)
    }



    fun saveToken(){
        com.rontaxi.cache.saveToken(app,UpdateDriverPhoneLiveData.value!!.body()!!.data!!.loginToken)
    }

    fun getUserProfile():User{

        return getUser(app)!!
    }


}