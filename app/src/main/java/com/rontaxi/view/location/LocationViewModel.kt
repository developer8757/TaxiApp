package com.rontaxi.view.location

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.location.Address
import android.location.Location
import android.util.Log
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places.createClient
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import java.util.*
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest






class LocationViewModel(app: Application): AndroidViewModel(app) {

    var results=MutableLiveData<List<AutocompletePrediction>>()
    var token = AutocompleteSessionToken.newInstance()
    var placesClient:PlacesClient?=null

    var addressfromPrediction=MutableLiveData<com.rontaxi.model.map.Address>()

    var TAG="LOCATION"


    var currentLocation: Location?=null






    init {


        placesClient=createClient(app)



    }


    val locationProvider: ReactiveLocationProvider by lazy {
        ReactiveLocationProvider(app)
    }

    fun getLocationWithSearch(searchQuery: String){
        locationProvider
            .getGeocodeObservable(searchQuery, 3)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<List<Address>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: List<Address>) {


                 //   results.postValue(t)

                    Log.i("Location Search",t.size.toString())


                }

                override fun onError(e: Throwable) {
                }
            })

    }


    fun getAddressPredictions(query: String){

     getAddressesObservable(query).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<List<AutocompletePrediction>>{

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: List<AutocompletePrediction>) {

                    results.postValue(t)
                }

                override fun onError(e: Throwable) {
                }
            })



    }


    private fun getAddressesObservable(query: String): Observable<List<AutocompletePrediction>>{

        val observable=Observable.create(ObservableOnSubscribe<List<AutocompletePrediction>> { emitter ->
            val bounds = RectangularBounds.newInstance(LatLng((currentLocation?.latitude)!!.minus(0.5f), (currentLocation?.longitude)!!.minus(0.5f)),
                LatLng((currentLocation?.latitude)!!.plus(0.5f), (currentLocation?.longitude)!!.plus(0.5f)));

            val request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                //.setLocationRestriction(bounds)

                // .setTypeFilter(TypeFilter.REGIONS)
                .setSessionToken(token)
                .setQuery(query)
                .build()

            placesClient!!.findAutocompletePredictions(request).addOnSuccessListener { response ->
                for (prediction in response.autocompletePredictions) {
                    Log.i(TAG, prediction.placeId)
                    Log.i(TAG, prediction.getPrimaryText(null).toString())
                }
                emitter.onNext(response.autocompletePredictions)
                emitter.onComplete()
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    val apiException = exception as ApiException
                    Log.e(TAG, "Place not found: " + apiException.statusCode)

                    results.postValue(listOf())
                }
            }
        })

        return observable

    }


    private fun getPlaceObservable(placeId: String): Observable<com.google.android.libraries.places.api.model.Place>{


        var observable=Observable.create(ObservableOnSubscribe<Place> {

            var emitter=it

            val placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
// Construct a request object, passing the place ID and fields array.
            var request = FetchPlaceRequest.builder(placeId, placeFields).build();




            placesClient!!.fetchPlace(request).addOnSuccessListener {

                var place=it.place
                emitter.onNext(place)

                emitter.onComplete()


            }.addOnFailureListener {


            }

        })

       return observable
    }


    fun getPlaceDetails(placeId: String) {

       getPlaceObservable(placeId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<Place> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Place) {
                    var address=com.rontaxi.model.map.Address()
                    address.address=t.address
                    address.lat=t.latLng?.latitude
                    address.lng=t.latLng?.longitude


                    addressfromPrediction.postValue(address)
                }

                override fun onError(e: Throwable) {
                }
            })

    }


    fun getLastLocation() {

        try {
           locationProvider.lastKnownLocation.subscribe {

                it?.let {

                    currentLocation=it

                }
            }
        } catch (e: SecurityException) {
            // exception
        }
    }

    override fun onCleared() {
        super.onCleared()

    }



}