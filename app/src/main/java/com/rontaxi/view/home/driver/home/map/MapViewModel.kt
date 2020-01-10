package com.rontaxi.view.home.driver.home.map

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import com.rontaxi.cache.saveDriverOnlineStatus
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import com.rontaxi.view.home.rider.godview.GetCancelReasonsLiveData
import io.reactivex.disposables.Disposable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

class MapViewModel(val app: Application) : AndroidViewModel(app) {

    val locationProvider: ReactiveLocationProvider by lazy {
        ReactiveLocationProvider(app)
    }

    lateinit var driverAvailabilityLiveData: DriverAvailabilityLiveData

    lateinit var updateBookingStatusLiveData: UpdateBookingStatusLiveData


    var locationDisposable: Disposable? = null

    var currentLocation = MutableLiveData<Location>()

    lateinit var getCancellationReasonsLiveData: GetCancelReasonsLiveData


    fun getCancellationReasons(){
        getCancellationReasonsLiveData.getCancellationReasons(1)

    }



    /**
     * fetching last known location
     */
    fun getLastLocation() {

        try {
            locationDisposable = locationProvider.lastKnownLocation.subscribe {

                it?.let {
                    currentLocation.postValue(it)


                }
            }
        } catch (e: SecurityException) {
            // exception
        }
    }


    fun changeDriverStatus(status: Boolean) {

        driverAvailabilityLiveData.updateDriverStatus(status)
    }


    fun onWay() {

        BookingManager?.currentBooking?.let {
            updateBookingStatusLiveData.updateBookingStatus(
                BookingManager.currentBooking!!.bookingId,
                UpdateBookingStatusLiveData.UpdateBookingActions.ON_WAY, null
            )
        }

    }


    fun arrived() {
        BookingManager?.currentBooking?.let {
            updateBookingStatusLiveData.updateBookingStatus(
                BookingManager.currentBooking!!.bookingId,
                UpdateBookingStatusLiveData.UpdateBookingActions.ARRIVED, null
            )
        }
    }

    fun startTrip() {
        BookingManager?.currentBooking?.let {
            updateBookingStatusLiveData.updateBookingStatus(
                BookingManager.currentBooking!!.bookingId,
                UpdateBookingStatusLiveData.UpdateBookingActions.STARTED, null
            )
        }
    }

    fun completeTrip() {
        BookingManager?.currentBooking?.let {
            updateBookingStatusLiveData.updateBookingStatus(
                BookingManager.currentBooking!!.bookingId,
                UpdateBookingStatusLiveData.UpdateBookingActions.COMPLETED, null
            )
        }
    }


    fun cancelBooking(reasonId: String?){
        BookingManager?.currentBooking?.let {
            updateBookingStatusLiveData.updateBookingStatus(
                BookingManager.currentBooking!!.bookingId,
                UpdateBookingStatusLiveData.UpdateBookingActions.CANCELLED_BY_DRIVER,
                reasonId
            )
            // cancelBookingLiveData.cancelBooking(BookingManager.BOOKING_ID)
        }
    }



    fun saveDriverStatus(status: Boolean) {

        saveDriverOnlineStatus(app, status!!)
    }

}