package com.rontaxi.model.nearbycabs

import com.rontaxi.model.locationupdate.LocationUpdateArrayElement


class NearByCabsResponseModel {

    var data = ArrayList<CabsDetails>()

}


class CabsDetails {

    var carType = ""
    var carId = ""
    var ETA = ""
    var isSelected = false
    var near = ArrayList<Near>()
    var image = ""
    var description = ""
    var baseFare = 0.0
    var perMileFareAmount = 0.0
    var passengerCapacity = 0
    var farePrice = ""

}


class Near {

    var location: ArrayList<LocationUpdateArrayElement>? = null
    var driverId = ""
    var brand = ""
    var year = ""

}

class NearLocation {

    var type = ""
    var coordinates = ArrayList<Double>()

}

