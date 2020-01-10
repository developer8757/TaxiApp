package com.rontaxi.model

import com.rontaxi.model.registration.Phone

class DriverModel {
    var userId = ""
    var firstName = ""
    var lastName = ""
    var phone: Phone? = null
    var rating = 0.0
    var email = ""
    var status = 0
    var carObj: CarObj? = null
    var driverImage = ""
    var brand = ""
    var model = ""
    var numberPlate = ""
    var color = ""
    var isBlocked=false
}

class CarObj {
    var name=""
    var carType = ""
    var passengerCapacity = 0
    var image = ""
    var commission = ""

}


