package com.rontaxi.view.initial.rider.register

import com.rontaxi.BuildConfig
import com.rontaxi.model.registration.Device
import com.rontaxi.model.registration.Phone
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterRequestModel{


    var device: RequestBody?=null//Device()


    var firstName: RequestBody?=null
    var lastName: RequestBody?=null
    var email: RequestBody?=null
    var phone: RequestBody?=null//Phone()

    var userActiveRole: RequestBody?=null
    var password: RequestBody?=null





}