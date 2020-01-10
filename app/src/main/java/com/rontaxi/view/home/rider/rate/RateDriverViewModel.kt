package com.rontaxi.view.home.rider.rate

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.model.RateRequestModel

class RateDriverViewModel(app: Application) : AndroidViewModel(app) {

    lateinit var ratingLiveData: RatingLiveData

    fun rateDriver(rating: Int, bookingId: String, comment: String) {

        val request = RateRequestModel()
        request.rating = rating
        request.bookingId = bookingId

        if (comment.isNotEmpty()) {
            request.comment = comment
        }
        ratingLiveData.rating(request)
    }

}