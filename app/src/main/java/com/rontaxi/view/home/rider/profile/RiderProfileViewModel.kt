package com.rontaxi.view.home.rider.profile

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.cache.getUser
import com.rontaxi.model.user.User

class RiderProfileViewModel(val app: Application): AndroidViewModel(app) {

    lateinit var updateProfileLiveData: UpdateProfileLiveData


    fun getUserProfile():User{

        return getUser(app)!!
    }

    fun updateProfile(updateProfileRequestModel: UpdateProfileRequestModel){
        updateProfileLiveData.updateProfile(updateProfileRequestModel)
    }


    fun saveUser(user: User){

        com.rontaxi.cache.saveUser(app,user)
    }


}