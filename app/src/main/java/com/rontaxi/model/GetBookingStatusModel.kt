package com.rontaxi.model

import com.dizzipay.railsbank.base.BaseResponseModel


class GetBookingStatusResponseModel: BaseResponseModel(){


    var type=""
    var data: GetBookingStatusData?=null

}

    class GetBookingStatusData{


        var bookingObj: Booking?=null
    }