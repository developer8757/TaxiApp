package com.rontaxi.view.home.rider.schedulebooking

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.model.locationupdate.LocationUpdateArrayElement
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.view.home.rider.godview.CreateBookingLiveData
import com.rontaxi.view.home.rider.godview.GetNearByCarsLiveData
import com.rontaxi.view.home.rider.godview.GetRoutesWithFairLiveData
import com.rontaxi.view.home.rider.ridehistory.RideHistoryLiveData
import java.util.*


class ScheduleBookingViewModel(val app: Application) : AndroidViewModel(app) {

    lateinit var getRoutesWithFairLiveData: GetRoutesWithFairLiveData

    lateinit var scheduleBookingLiveData: ScheduleBookingLiveData


    fun getFairWithEta(
        pickUpLocation: com.rontaxi.model.map.Address,
        dropOffLocation: com.rontaxi.model.map.Address
    ) {

        val pickUpLoc = LocationUpdateArrayElement()
        pickUpLoc.lat = pickUpLocation.lat
        pickUpLoc.log = pickUpLocation.lng


        val dropOffLoc = LocationUpdateArrayElement()
        dropOffLoc.lat = dropOffLocation.lat
        dropOffLoc.log = dropOffLocation.lng

        getRoutesWithFairLiveData.getRoutesWithEtaAndPrice(null, pickUpLoc, dropOffLoc)

    }

    fun createScheduleBooking(
        selectedCab: CabsDetails,
        pickUpAddress: com.rontaxi.model.map.Address,
        dropOffAddress: com.rontaxi.model.map.Address,
        tentativePrice: String, scheduledDate: Date

    ) {
        scheduleBookingLiveData.scheduleBooking(
            selectedCab,
            pickUpAddress,
            dropOffAddress,
            tentativePrice,
            scheduledDate
        )

    }
}