package com.rontaxi.view.initial.rider.otp

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.socket.SocketManager

class OtpViewModel (val app: Application): AndroidViewModel(app) {

    lateinit var otpVerificationLiveData: OtpVerificationLiveData

    lateinit var resendOtpLiveData: ResendOtpLiveData

    lateinit var socketManager: SocketManager


    fun resendOtp(){

        resendOtpLiveData.resendOtp()
    }

    fun verifyOtp(otp: String, token: String?){
        otpVerificationLiveData.otpVerification(otp,token)

    }

    fun connectSockets(){
        socketManager.connect()
    }

    fun saveToken(){
        com.rontaxi.cache.saveToken(app,otpVerificationLiveData.value!!.body()!!.data!!.loginToken)

    }

    fun saveUser(){
        com.rontaxi.cache.saveUser(app,otpVerificationLiveData.value!!.body()!!.data!!.userObj!!)
    }

}