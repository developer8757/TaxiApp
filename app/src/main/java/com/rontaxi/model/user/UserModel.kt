package com.rontaxi.model.user

import com.rontaxi.model.registration.Phone
import com.rontaxi.view.home.rider.payment.Payment
import com.rontaxi.view.initial.driver.vehicletype.CarType

class   User{

    var userId = ""

    var firstName = ""

    var lastName = ""

    var email = ""

    var phone: Phone? = null

    var rating=0.0

    var lastLogin: Number=0

    var userActiveRole=1

    var profileImage=""

    var driverImage = ""

    var status = 0

    var brand = ""
    var model = ""
    var year = ""
    var carNumberId = ""
    var numberPlate = ""
    var color = ""
    var permitDoc = ""
    var insuranceDoc = ""
    var licenseNumber = ""
    var licenseDoc = ""
    var licenseIssuedOn = ""
    var licenseExpiryDate = ""
    var regDoc = ""
    var vehicleImage = ""

    var carType: String = ""
    var carObj: CarType? = null

    var payment: ArrayList<Payment>?=null

    var isAccountAdded=0






}




