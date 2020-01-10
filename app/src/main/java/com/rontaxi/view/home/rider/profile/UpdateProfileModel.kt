package com.rontaxi.view.home.rider.profile

import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateProfileRequestModel {

    var profilePhoto: MultipartBody.Part?=null
    var firstName: RequestBody? = null
    var lastName: RequestBody? = null
    var email: RequestBody? = null
    var phone: RequestBody? = null//Phone()
    var driverImage: MultipartBody.Part? = null
}