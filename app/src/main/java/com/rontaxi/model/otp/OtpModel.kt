package com.rontaxi.model.otp

import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.model.user.User




class OtpResponse: BaseResponseModel(){

    var data: Data?=null


}

class Data{

    var userObj:User?=null
    var loginToken=""
}