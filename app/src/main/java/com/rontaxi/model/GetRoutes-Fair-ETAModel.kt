package com.rontaxi.model

import com.rontaxi.model.locationupdate.LocationUpdateArrayElement


class GetRouteEtaFairModel{


    var fare: Fair?=null

    var ETA: String?=null

    var distance=""

    var route: Route?=null



   // var route: ArrayList<RouteElement>?=null
}


class Route{

    var steps: ArrayList<Step>?=null

}

class Distance{

    var text=""



}




class RouteElement{

    var endLocation:LocationUpdateArrayElement?=null
    var startLocation: LocationUpdateArrayElement?=null


}

class Fair{



    var currency: CurrencyModel?=null

    var carFare: ArrayList<CarFare>?=null

}


class CarFare{

    var name=""
    var fare=0.0
}



class Step{

    var end_location:LatLng?=null
    var start_location: LatLng?=null

    var polyline: Polyline?=null
}

class Polyline{

    var points=""
}



class LatLng{

    var lat=0.0
    var lng=0.0

}


