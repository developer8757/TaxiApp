package com.rontaxi.view.home.driver.home

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.LocationRequest
import com.rontaxi.RonTaxiApp
import com.rontaxi.model.locationupdate.LocationUpdateArrayElement
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.map.GetAvailabiltyStatusLiveData
import com.rontaxi.view.home.rider.godview.BookingStatusLiveData
import com.rontaxi.view.home.rider.godview.GetBookingStatusLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.rontaxi.location.LocationUpdateService


class HomeDriverViewModel(val app: Application) : AndroidViewModel(app) {

    lateinit var driverLocationUpdateLiveData: DriverLocationUpdateLiveData

    lateinit var acceptRejectRequestLiveData: AcceptRejectLiveData
    lateinit var notifyDriverLiveData: NotifyDriverLiveData

    lateinit var getBookingStatusLiveData: GetBookingStatusLiveData

    lateinit var bookingStatusUpdateLiveData: BookingStatusLiveData


    var currentLocation = MutableLiveData<Location>()

    var arrayListLocations = ArrayList<LocationUpdateArrayElement>()

    lateinit var availabilityStatusLiveData: GetAvailabiltyStatusLiveData
    lateinit var getBookingLiveData: GetBookingLiveData

    val locationProvider: ReactiveLocationProvider by lazy {
        ReactiveLocationProvider(app)
    }

    var locationDisposable: Disposable? = null

    fun getLocationUpdates() {

        val request = LocationRequest.create() //standard GMS LocationRequest
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            // .setSmallestDisplacement(2f)
            .setInterval(5000)

        //.setSmallestDisplacement(10.0f)

        try {

            locationDisposable = locationProvider.getUpdatedLocation(request)
                .subscribe(Consumer {

                    currentLocation.postValue(it)

                    var arrayElement = LocationUpdateArrayElement()
                    arrayElement.lat = it.latitude
                    arrayElement.log = it.longitude
                    arrayElement.bearing = it.bearing


                    arrayListLocations.add(arrayElement)


                    if (arrayListLocations.size > 3) {
                        updateLocationToServer()

                    }

                })

        } catch (e: SecurityException) {

        }
    }


    private fun updateLocationToServer() {


        driverLocationUpdateLiveData.updateLocationToServer(arrayListLocations)

        arrayListLocations.clear()


    }


    override fun onCleared() {
        super.onCleared()

        locationDisposable?.let {

            it.dispose()
        }
    }

    fun getAvailabilityUpdate() {

        availabilityStatusLiveData.getAvailabiltyStatus()
    }


    fun startListeningNotifyDriver() {

        notifyDriverLiveData.startListening()

    }


    fun updateBookingStatus(bookingId: String, status: AcceptRejectLiveData.AcceptRejectAction) {

        acceptRejectRequestLiveData.updateBookingStatus(bookingId, status)
    }

    fun getBookingStatus() {

        getBookingStatusLiveData.getBookingStatus()
    }


    fun startListeningBookingStatus() {

        bookingStatusUpdateLiveData.startListeningBookingStatus()
    }




}