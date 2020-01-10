package com.rontaxi.model.driverrating


data class DriverRatingResponseModel(
    val `data`: Data,
    val message: String,
    val statusCode: Int
)

data class Data(
    val average: Double,
    val fiveStar: Int,
    val fourStar: Int,
    val limit: Int,
    val oneStar: Int,
    val ratingData: List<RatingData>,
    val threeStar: Int,
    val total: Int,
    val twoStar: Int
)

data class
RatingData(
    val bookingId: String,
    val date: String,
    val profilePhoto: String,
    val rating: Int,
    val reviewText: String,
    val riderName: String
)
