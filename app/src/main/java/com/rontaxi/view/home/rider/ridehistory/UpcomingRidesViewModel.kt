package com.rontaxi.view.home.rider.ridehistory

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import com.rontaxi.view.home.rider.godview.GetCancelReasonsLiveData
import com.rontaxi.view.home.rider.help.HelpReasonsLiveData
import com.rontaxi.view.home.rider.payment.BlockDriverLiveData

class UpcomingRidesViewModel(app: Application) : AndroidViewModel(app) {


    lateinit var getCancellationReasonsLiveData: GetCancelReasonsLiveData

    lateinit var updateBookingStatusLiveData: UpdateBookingStatusLiveData


    fun getCancellationReasons() {


        getCancellationReasonsLiveData.getCancellationReasons(1)


    }

    fun cancelBooking(bookingId: String, reasonId: String?) {

        updateBookingStatusLiveData.updateBookingStatus(
            bookingId,
            UpdateBookingStatusLiveData.UpdateBookingActions.CANCELLED_BY_CUSTOMER,
            reasonId
        )
        // cancelBookingLiveData.cancelBooking(BookingManager.BOOKING_ID)

    }

}