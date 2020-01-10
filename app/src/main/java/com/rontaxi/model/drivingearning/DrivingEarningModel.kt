package com.rontaxi.model.drivingearning

import com.rontaxi.model.user.User


data class DriverEarningModel(
    val data: DrivingEarningData,
    val message: String,
    val statusCode: Int
)

data class DrivingEarningData(
    val booking: ArrayList<DriverEarningBooking>,
    val completedRides: Int,
    val limit: Int,
    val timeSpent: Int,
    val totalCount: Int,
    val totalEarning: TotalEarning
)

data class TotalEarning(
    val totalCost: Int,
    val displayAmount: String
)

data class DriverEarningBooking(
    val bookingId: String,
    val bookingNumber: String,
    val bookingStatus: Int,
    val createdAt: Long,
    val driverObj: DriverObj,
    val dropAddress: String,
    val duration: Int,
    val endingPoint: EndingPoint,
    val paymentMethod: Int,
    val pickupAddress: String,
    val riderObj: RiderObj,
    val routes: Routes,
    val startingPoint: StartingPoint,
    val tentativeBudget: String,
    val totalCost: Int,
    val displayAmount: String

)

data class RiderObj(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: PhoneX,
    val rating: Double,
    val status: Int,
    val userId: String,
    val profileImage: String
)

data class Phone(
    val code: String,
    val number: String
)

data class StartingPoint(
    val bearing: Int,
    val lat: Double,
    val log: Double
)

data class Routes(
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val end_location: EndLocation,
    val start_address: String,
    val start_location: StartLocation,
    val steps: List<Step>,
    val traffic_speed_entry: List<Any>,
    val via_waypoint: List<Any>
)

data class StartLocation(
    val lat: Double,
    val lng: Double
)

data class EndLocation(
    val lat: Double,
    val lng: Double
)

data class Duration(
    val text: String,
    val value: Int
)

data class Distance(
    val text: String,
    val value: Int
)

data class Step(
    val distance: DistanceX,
    val duration: DurationX,
    val end_location: EndLocation,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
    val start_location: StartLocationX,
    val travel_mode: String
)

data class DurationX(
    val text: String,
    val value: Int
)

data class StartLocationX(
    val lat: Double,
    val lng: Double
)

data class Polyline(
    val points: String
)

data class DistanceX(
    val text: String,
    val value: Int
)

data class EndingPoint(
    val bearing: Int,
    val lat: Double,
    val log: Double
)

data class DriverObj(
    val brand: String,
    val color: String,
    val driverImage: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val model: String,
    val numberPlate: String,
    val phone: Phone,
    val rating: Double,
    val status: Int,
    val userId: String,
    val profileImage: String
)

data class PhoneX(
    val code: String,
    val number: String
)