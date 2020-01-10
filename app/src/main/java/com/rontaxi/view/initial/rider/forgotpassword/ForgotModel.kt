package com.rontaxi.view.initial.rider.forgotpassword

import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.BuildConfig
import com.rontaxi.model.registration.Phone

class ForgotRequest{

    var phone=Phone()
    var role=BuildConfig.ROLE

}


class ForgotResponse: BaseResponseModel(){


    var data: Data?=null
}


class Data{

    var loginToken=""
}