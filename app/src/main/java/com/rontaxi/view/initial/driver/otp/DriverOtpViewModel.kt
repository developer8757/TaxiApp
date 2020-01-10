package com.rontaxi.view.initial.driver.otp

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.initial.rider.otp.OtpVerificationLiveData
import com.rontaxi.view.initial.rider.otp.ResendOtpLiveData

class DriverOtpViewModel(val app: Application): AndroidViewModel(app) {

    lateinit var otpVerificationLiveData: OtpVerificationLiveData

    lateinit var resendOtpLiveData: ResendOtpLiveData

    lateinit var socketManager: SocketManager


    fun resendOtp(){

        resendOtpLiveData.resendOtp()
    }

    fun verifyOtp(otp: String,token:String?){

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