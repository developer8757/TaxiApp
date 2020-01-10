package com.rontaxi.model.login

import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.model.user.User

class LoginResponseModel: BaseResponseModel(){
    var data: Data?=null
}

class Data{
    var userObj: User?=null
    var loginToken=""
}

