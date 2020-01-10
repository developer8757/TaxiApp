package com.rontaxi.model.registration

import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.BuildConfig
import com.rontaxi.model.user.User
import retrofit2.http.Multipart
import retrofit2.http.Part

class RegistrationRequest{


    var firstName=""
    var lastName=""
    var email=""
    var phone=Phone()

    var driverDetails=DriverDetails()
    var password=""
    var role=BuildConfig.ROLE
}

class Phone{
    var code=""
    var number=""
}


class DriverDetails{

    var profileImage=""
    var permit=Permit()
    var insurance=Insurance()
    var license=License()
    var vehicleDetails=VehicleDetails()

}

class Permit{

    var permitNumber=""
    var permitDoc=""

}

class Insurance{

    var insuranceNumber=""
    var insuranceDoc=""
}
class License{

    var licenseNumber=""
    var licenseDoc=""
    var issuedOn=""
    var expiryDate=""
}


class VehicleDetails(){

    var registrationNumber=""
    var regDoc=""
    var vehicleImage=""
    var brand=""
    var model=""
    var year=""
    var color=""
    var carId=""


}


class Device{

    var token=""
    var type=0
}


class RegistrationResponseModel: BaseResponseModel(){

    var data: Data?=null
   // var userObj: User?=null

}

class Data{

    var loginToken=""
}

