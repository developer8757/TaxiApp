package com.rontaxi.view.home.driver.home.profile.basicinfo

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.cache.getUser
import com.rontaxi.cache.saveToken
import com.rontaxi.cache.saveUser
import com.rontaxi.model.user.User
import com.rontaxi.view.home.rider.profile.UpdateProfileRequestModel
import com.rontaxi.view.initial.driver.register.DriverRegisterRequest

class BasicInformationViewModel(val app: Application): AndroidViewModel(app) {

    lateinit var updateDriverProfileLiveData: UpdateDriverProfileLiveData

    fun getUserProfile(): User {
        return getUser(app)!!
    }


    fun updateDriverProfile(updateUserInfoModel: DriverRegisterRequest){
        updateDriverProfileLiveData.updateDriverProfile(updateUserInfoModel)
    }


    fun saveUser(){
        saveUser(app,updateDriverProfileLiveData.value!!.body()!!.data!!.userObj!!)
    }

    fun saveToken(){
        saveToken(app, updateDriverProfileLiveData.value!!.body()!!.data!!.loginToken)
    }
}