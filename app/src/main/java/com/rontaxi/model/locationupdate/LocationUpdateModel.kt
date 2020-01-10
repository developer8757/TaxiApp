package com.rontaxi.model.locationupdate



class LocationUpdateRequestModel{

    var location=ArrayList<LocationUpdateArrayElement>()

}



class LocationUpdateArrayElement {

    var lat: Double?=null
    var log: Double?=null
    var bearing: Float?=null

}

