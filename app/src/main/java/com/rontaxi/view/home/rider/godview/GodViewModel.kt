package com.rontaxi.view.home.rider.godview


import android.Manifest
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.location.Address

import android.location.Location
import android.support.v4.content.ContextCompat

import io.reactivex.disposables.Disposable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import com.google.android.gms.location.LocationRequest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.iid.FirebaseInstanceId
import com.rontaxi.cache.savePhone
import com.rontaxi.model.locationupdate.LocationUpdateArrayElement
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.model.registration.Phone
import com.rontaxi.view.home.driver.home.AcceptRejectLiveData
import com.rontaxi.view.home.driver.home.GetBookingLiveData
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import com.rontaxi.view.home.rider.ridehistory.RideHistoryLiveData
import com.rontaxi.view.initial.driver.login.LoginRequestModel
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeLiveData
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import java.util.*


class GodViewModel(val app: Application) : AndroidViewModel(app) {


    lateinit var getBookingStatus: GetBookingStatusLiveData

    lateinit var vehicleTypeLiveData: VehicleTypeLiveData
    lateinit var getNearByCarsLiveData: GetNearByCarsLiveData

    lateinit var getRoutesWithFairLiveData: GetRoutesWithFairLiveData

    lateinit var createBookingLiveData: CreateBookingLiveData

    //   lateinit var cancelBookingLiveData: CancelBookingLiveData

    lateinit var createBookingStatusLiveData: CreateBookingStatusLiveData

    lateinit var updateBookingStatusLiveData: UpdateBookingStatusLiveData

    lateinit var bookingStatusLiveData: BookingStatusLiveData

    lateinit var getCancellationReasonsLiveData: GetCancelReasonsLiveData

    lateinit var getSOSLiveData: GetSOSLiveData

    lateinit var getBookingLiveData: GetBookingLiveData


    var location = MutableLiveData<Location>()

    var currentLocation = MutableLiveData<Location>()
    var address = MutableLiveData<Address>()

    var placesClient: PlacesClient? = null

    val locationProvider: ReactiveLocationProvider by lazy {
        ReactiveLocationProvider(app)
    }


    lateinit var subscription: Subscription

    var locationDisposable: Disposable? = null


    init {
        placesClient = Places.createClient(app)
    }


    lateinit var rideHistoryLiveData: RideHistoryLiveData

    fun rideHistory(type: Int, offSet: Int) {
        rideHistoryLiveData.rideHistory(type, offSet)
    }

    /**
     * fetching last known location
     */
    fun getLastLocation() {

        try {
            locationDisposable = locationProvider.lastKnownLocation.subscribe {

                it?.let {
                    currentLocation.postValue(it)


                    //getCurrentPlace()

                    getAddress(it)
                }
            }
        } catch (e: SecurityException) {
            // exception
        }
    }


    override fun onCleared() {

        locationDisposable?.dispose()
        super.onCleared()
    }

    fun getBookingData(bookingId: String) {


        getBookingLiveData.getBookingData(bookingId)
    }


    fun getLocationUpdates() {

        val request = LocationRequest.create() //standard GMS LocationRequest
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)

        try {

            locationDisposable = locationProvider.getUpdatedLocation(request)

                .subscribe(Consumer {

                    location.postValue(it)


                    if (LocationChildFragment.autoUpdatePickUp) {

                        //getCurrentPlace()

                        getAddress(it)


                        val currentLocation = LocationUpdateArrayElement()
                        currentLocation.lat = it.latitude
                        currentLocation.log = it.longitude
                        //  currentLocation.bearing=it.bearing
                        getNearbyCars(currentLocation)

                    } else {

                        val currentLocation = LocationUpdateArrayElement()
                        currentLocation.lat = LocationChildFragment.pickUpAddress.value!!.lat
                        currentLocation.log = LocationChildFragment.pickUpAddress.value!!.lng
                        //  currentLocation.bearing=it.bearing
                        getNearbyCars(currentLocation)

                    }

                    // getAddress(it)
                })

        } catch (e: SecurityException) {

        }


    }


    fun getAddress(location: Location) {
        locationProvider.getReverseGeocodeObservable(location!!.latitude, location!!.longitude, 5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<List<Address>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: List<Address>) {

                    if (!t.isNullOrEmpty()) {


                        var address = com.rontaxi.model.map.Address()
                        address.address = t[0].getAddressLine(0)
                        address.lat = t[0].latitude//places[0].place.latLng?.latitude
                        address.lng = t[0].longitude//places[0].place.latLng?.longitude

                        LocationChildFragment.pickUpAddress.postValue(address)
                    }


                    //address.postValue(t[0])
                }

                override fun onError(e: Throwable) {
                }
            })

    }


    fun getCurrentPlace() {
        if (ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val placeFields =
                Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

            val request = FindCurrentPlaceRequest.builder(placeFields).build()


            var placeResponse = placesClient!!.findCurrentPlace(request);
            placeResponse.addOnCompleteListener {

                if (it.isSuccessful) {
                    var response = it.getResult()


                    var places = response!!.getPlaceLikelihoods()


                    var address = com.rontaxi.model.map.Address()
                    address.address = places[0].place.address
                    address.lat = places[0].place.latLng?.latitude
                    address.lng = places[0].place.latLng?.longitude

                    LocationChildFragment.pickUpAddress.postValue(address)

                }


            }.addOnFailureListener {


            }

        }
    }

    fun getBookingStatus() {

        getBookingStatus.getBookingStatus()
    }


    fun getCabTypes() {

        vehicleTypeLiveData.getCarType()

    }

    fun getNearbyCars(currentLocation: LocationUpdateArrayElement) {
        getNearByCarsLiveData.getNearByCars(currentLocation)
    }


    fun getFairWithEta(
        selectedCab: CabsDetails,
        pickUpLocation: com.rontaxi.model.map.Address,
        dropOffLocation: com.rontaxi.model.map.Address
    ) {

        val pickUpLoc = LocationUpdateArrayElement()
        pickUpLoc.lat = pickUpLocation.lat
        pickUpLoc.log = pickUpLocation.lng


        val dropOffLoc = LocationUpdateArrayElement()
        dropOffLoc.lat = dropOffLocation.lat
        dropOffLoc.log = dropOffLocation.lng

        getRoutesWithFairLiveData.getRoutesWithEtaAndPrice(selectedCab.carId, pickUpLoc, dropOffLoc)

    }


    fun createBooking(
        selectedCab: CabsDetails,
        pickUpAddress: com.rontaxi.model.map.Address,
        dropOffAddress: com.rontaxi.model.map.Address,
        tentativePrice: String
    ) {
        createBookingLiveData.createBooking(
            selectedCab,
            pickUpAddress,
            dropOffAddress,
            tentativePrice
        )

    }

    fun cancelBooking(reasonId: String?) {

        BookingManager.currentBooking?.let{
            updateBookingStatusLiveData.updateBookingStatus(
                it.bookingId,
                UpdateBookingStatusLiveData.UpdateBookingActions.CANCELLED_BY_CUSTOMER,
                reasonId
            )

        }

        // cancelBookingLiveData.cancelBooking(BookingManager.BOOKING_ID)

    }

    fun startListeningCreateBookingStatus() {

        createBookingStatusLiveData.startListeningCreateBookingStatus()

    }


    fun startListeningBookingStatus() {

        bookingStatusLiveData.startListeningBookingStatus()
    }


    fun getCancellationReasons() {


        getCancellationReasonsLiveData.getCancellationReasons(1)


    }


    //  lateinit var socketManager: SocketManager


}