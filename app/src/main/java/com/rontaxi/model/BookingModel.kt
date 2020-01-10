package com.rontaxi.model

import com.rontaxi.model.user.User

class Booking {

    var bookingId = ""

    var bookingStatus = 0

    var createdAt = ""

    var scheduledDate = ""

    var driverObj: DriverModel? = null

    var rating = 3

    var dropAddress = ""

    var comment = ""

    var endingPoint: Point? = null

    var totalCost = 1.0F

    var distace = 0

    var paymentMethod = 0

    var pickupAddress = ""

    var riderObj: User? = null

    var startingPoint: Point? = null

    var tentativeBudget = ""
    var displayAmount = ""

    var routes: Route? = null
    var driverLatLog: ArrayList<Point>? = null

    var serverTime=0L





}

class Point {

    var lat = 0.0

    var log = 0.0
}



