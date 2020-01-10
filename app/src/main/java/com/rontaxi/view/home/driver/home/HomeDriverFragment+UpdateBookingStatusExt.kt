package com.rontaxi.view.home.driver.home

import com.rontaxi.model.manager.BookingManager


fun HomeDriverFragment.acceptBooking(){

    homeDriverViewModel.updateBookingStatus(BookingManager.currentBooking!!.bookingId,AcceptRejectLiveData.AcceptRejectAction.ACCEPT)

}

fun HomeDriverFragment.rejectBooking(){

    homeDriverViewModel.updateBookingStatus(BookingManager.currentBooking!!.bookingId,AcceptRejectLiveData.AcceptRejectAction.REJECT)

}


fun HomeDriverFragment.setUpdateBookingObserver(){


}