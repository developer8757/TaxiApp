package com.rontaxi.model

import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.model.locationupdate.LocationUpdateArrayElement
import java.sql.Timestamp

class CreateBookingRequestModel {

    var startingPoint = LocationUpdateArrayElement()
    var endingPoint = LocationUpdateArrayElement()

    var carId = ""
    var pickupAddress = ""
    var dropAddress = ""
    var tentativeBudget = ""
    var paymentMethod = 0

    var scheduleDate: Long? = 0


}


class CreateBookingResponseModel {

    // var bookingNumber=""
    var _id = ""

}

class Data {


    var bookingObj: Booking? = null


}

class CreateBookingStatusResponseModel : BaseResponseModel() {

    var data: Data? = null


}


