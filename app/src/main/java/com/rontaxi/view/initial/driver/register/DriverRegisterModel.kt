package com.rontaxi.view.initial.driver.register

import com.rontaxi.BuildConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

class DriverRegisterRequest{
    var device: RequestBody?=null//Device()


    var firstName: RequestBody?=null
    var lastName: RequestBody?=null
    var email: RequestBody?=null
    var phone: RequestBody?=null//Phone()
    var driverImage: MultipartBody.Part?=null
    var userActiveRole: RequestBody?=null
    var password: RequestBody?=null

    // license details
    var licenseDoc: MultipartBody.Part?=null
    var licenseNumber: RequestBody?=null
    var licenseIssuedOn: RequestBody?=null
    var licenseExpiryDate: RequestBody?=null

    // vehicle detials
    var vehicleImage: MultipartBody.Part?=null
    var brand: RequestBody?=null
    var model: RequestBody?=null
    var year: RequestBody?=null
    var color: RequestBody?=null
    var carNumberId: RequestBody?=null
    var carId: RequestBody?=null
    var regDoc: MultipartBody.Part?=null
    var insuranceDoc:MultipartBody.Part?=null
    var permitDoc: MultipartBody.Part?=null
}