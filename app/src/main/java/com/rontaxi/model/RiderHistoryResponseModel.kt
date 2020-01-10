package com.rontaxi.model

import com.dizzipay.railsbank.base.BaseResponseModel

class RiderHistoryResponseModel : BaseResponseModel() {
    var data: DataRiderHistory? = null
}

class DataRiderHistory {
    var bookingObj: ArrayList<Booking>? = null
    var totalCount = 0
    var limit = 0
}