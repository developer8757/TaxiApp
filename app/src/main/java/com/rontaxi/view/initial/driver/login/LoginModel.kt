package com.rontaxi.view.initial.driver.login

import com.rontaxi.BuildConfig
import com.rontaxi.model.registration.Device
import com.rontaxi.model.registration.Phone

class LoginRequestModel{
    var phone=Phone()
    var password=""
    var device=Device()
    var role= BuildConfig.ROLE.toString()
}