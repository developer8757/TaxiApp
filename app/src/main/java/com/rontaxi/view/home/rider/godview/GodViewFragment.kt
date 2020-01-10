package com.rontaxi.view.home.rider.godview


import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.rontaxi.base.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.rontaxi.R
import com.rontaxi.cache.currentSocketState
import com.rontaxi.cache.getSOSNumber
import com.rontaxi.cache.saveSOSNumber
import com.rontaxi.model.Booking
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.model.nearbycabs.Near
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.socket.SocketManager
import com.rontaxi.util.NotificationHelper
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.cancelNotification
import com.rontaxi.util.showAlert
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.rate.RateDriverFragment

import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class GodViewFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

    var selectedCab: CabsDetails? = null

    var nearByCabsArray = ArrayList<Near>()

    var markerArray = HashMap<String, Marker>()

    var sedanMarker: BitmapDescriptor? = null

    var polyLine: Polyline? = null

    var pickUpMarker: Marker? = null
    var dropOffMarker: Marker? = null


    var customInfoWindowPickUpView: View? = null


    var searchDriverDialog: SearchDriverDialog? = null

    var cancellationDialog: CancellationReasonsDialog? = null


    val locationChildFragment: LocationChildFragment by lazy {
        LocationChildFragment()
    }


    val supportMapFragment: SupportMapFragment by lazy {

        SupportMapFragment()
    }


    companion object {

        val cabListChildFragment: CabListChildFragment by lazy {

            CabListChildFragment()
        }

        var LOCATION_REQUEST_CODE = 201

        var upcomingBookings: ArrayList<Booking>? = null

    }

    val driverDetailsChildFragment: DriverDetailsChildFragment by lazy {

        DriverDetailsChildFragment()

    }


    override fun getLayoutRes() = R.layout.fragment_home

    override fun showTitleBar() = false
    lateinit var mMap: GoogleMap


    @Inject
    lateinit var godViewModel: GodViewModel

    val compDispPerm: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    val rxPermission: RxPermission by lazy {
        RealRxPermission.getInstance(activity!!.application)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isLoaded) {
            return
        }

        customInfoWindowPickUpView =
            activity!!.layoutInflater.inflate(R.layout.window_marker_pickup, null)

        setObservers()

        changeChildFragment(locationChildFragment)

        setClicks()
        initMap()


        /**
         * if home activity is opened from notification for the booking status completed
         * than get the booking details and open rate driver fragment
         */
        if (activity?.intent?.hasExtra("bookingId")!!) {


            if (BookingManager.currentBooking == null) {

                var bookingId = activity?.intent?.getStringExtra("bookingId")

                //get booking details
                godViewModel.getBookingData(bookingId!!)

            }

        }


        // godViewModel.getCabTypes()


    }

    fun getUpComingRides() {
        /**
         * get upcoming booking if any
         */

        godViewModel.rideHistory(1, 0)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap.setOnCameraChangeListener(this)
        getCurrentLocation()

    }

    private fun setClicks() {


        ibDrawer.setOnClickListener {

            (activity as HomeRiderActivity).openCloseDrawer()
        }

        ibMyLocation.setOnClickListener {
            godViewModel.getLastLocation()


        }


        ibBack.setOnClickListener {
            onBackPressed()
        }

        btnSOS.setOnClickListener {

            com.rontaxi.util.makeTelephoneCall(
                context!!, getSOSNumber(context!!)
            )

        }


    }

    private fun setObservers() {


        godViewModel.getBookingLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()

            if (it!!.isSuccessful) {
                if (isResponseValid(activity!!, it.body()!!)) {


                    Toasty.success(context!!, it.body()!!.message).show()


                    BookingManager.currentBooking = null
                    polyLine?.remove()
                    pickUpMarker?.remove()
                    dropOffMarker?.remove()
                    LocationChildFragment.dropOffAddress.value = null
                    pickUpMarker = null
                    dropOffMarker = null

                    selectedCab = null

                    changeChildFragment(locationChildFragment)


                    var driverRatingFragment = RateDriverFragment()
                    driverRatingFragment.booking = it.body()!!.data!!


                    (activity as HomeRiderActivity).replaceFragment(driverRatingFragment, true)
                    btnSOS.visibility = View.GONE
                }

            } else {

                //  tackleErrorNavigation(activity!!,it.errorBody()!!)

                showAlert(
                    context!!,
                    getErrorMessage(it.errorBody()!!).message,
                    getString(R.string.ok),
                    {})
            }

        })


        currentSocketState.observe(this, Observer {

            when (it) {

                SocketManager.SOCKET_STATE.AUTHENTICATED -> {

                    godViewModel.getBookingStatus()

                    godViewModel.getSOSLiveData.getSOS()


                }
            }


        })

        godViewModel.getBookingStatus.observe(this, Observer {

            it?.data?.bookingObj?.let {


                BookingManager.currentBooking = it


                updateView(it)


            }

            // to do

        })

        /*godViewModel.vehicleTypeLiveData.observe(this, Observer {

            if(it!!.isSuccessful){

            }
        })*/

        godViewModel.getNearByCarsLiveData.observe(this, Observer {

            cabListChildFragment.cabDataArray = it!!.data

            if (selectedCab == null) {
                selectedCab = it.data[0]
                selectedCab?.isSelected = true
            }


            selectedCab = it.data.singleOrNull {

                selectedCab!!.carId.equals(it.carId)
            }

            if (selectedCab == null) {
                selectedCab = it.data[0]
                selectedCab?.isSelected = true
            }

            nearByCabsArray.clear()
            nearByCabsArray.addAll(selectedCab!!.near)




            moveCabs()


            pickUpMarker?.apply {

                if (BookingManager.currentBooking == null) {

                    setPickUpMarker()
                }
            }


        })


        godViewModel.getRoutesWithFairLiveData.observe(this, Observer {


            changeChildFragment(cabListChildFragment)


            drawPolyLine(it!!.route?.steps!!)


            cabListChildFragment.updateFare(it)


        })


        godViewModel.createBookingLiveData.observe(this, Observer {

            it?.let {

                ProgressDialog.hideProgressBar()

                BookingManager.currentBooking = it

                searchDriverDialog = SearchDriverDialog(context!!)
                searchDriverDialog!!.searchDriverDialogInterface =
                    object : SearchDriverDialog.SearchDriverDialogInterface {
                        override fun onCancelBooking() {
                            godViewModel.cancelBooking(null)
                        }
                    }
                searchDriverDialog!!.show()

            }

        })



        godViewModel.startListeningCreateBookingStatus()

        godViewModel.createBookingStatusLiveData.observe(this, Observer {

            it?.let {

                searchDriverDialog?.dismiss()

                if (it.statusCode == 200) {


                    BookingManager.currentBooking = it.data?.bookingObj
                    it.data?.bookingObj?.let { it1 ->
                        updateView(it1)
                    }


                } else {

                    showAlert(context!!, it.message, getString(R.string.ok)) {


                        // Back to initial state

                    }

                }


            }

        })

        godViewModel.startListeningBookingStatus()

        godViewModel.bookingStatusLiveData.observe(this, Observer {


            it?.data?.bookingObj?.let {
                updateView(it)

            }


        })


        godViewModel.updateBookingStatusLiveData.observe(this, Observer {

            it?.data?.bookingObj?.let {

                updateView(it)
            }
        })

        godViewModel.getCancellationReasonsLiveData.observe(this, Observer {


            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                showCancellationDialog(it.body()?.data!!)


            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }


        })


        godViewModel.getSOSLiveData.observe(this, Observer {


            it?.data?.let {
                saveSOSNumber(context!!, it.sosNumber)

            }


        })

        godViewModel.rideHistoryLiveData.observe(this, Observer {
            it!!.apply {
                ProgressDialog.hideProgressBar()
                if (it.isSuccessful) {
                    if (it!!.isSuccessful) {
                        upcomingBookings = it.body()?.data?.bookingObj

                        locationChildFragment.updateData(upcomingBookings)
                    }
                }
            }
        })


    }

    override fun onResume() {
        super.onResume()

        // get latest upcoming rides in case of new scheduled ride , cancel scheduled ride , expires booking

        getUpComingRides()
    }

    fun changeChildFragment(fragment: Fragment) {

        childFragmentManager.beginTransaction()
            .replace(R.id.childContainer, fragment, fragment::class.java.canonicalName).commit()
    }


    fun onBackPressed() {

        val currentChildFragment = childFragmentManager.findFragmentById(R.id.childContainer)

        when (currentChildFragment) {

            is CabListChildFragment -> {
                LocationChildFragment.dropOffAddress.value = null

                polyLine?.remove()
                pickUpMarker?.remove()
                dropOffMarker?.remove()

                pickUpMarker = null
                dropOffMarker = null

                LocationChildFragment.autoUpdatePickUp = true

                changeChildFragment(locationChildFragment)

            }

            is LocationChildFragment -> {

                activity!!.finish()
            }


            is DriverDetailsChildFragment -> {


            }

        }

    }

    fun onSelectedCabTypeChanged(slctdCab: CabsDetails) {

        selectedCab = slctdCab

        nearByCabsArray.clear()

        markerArray.forEach {

            it.value.remove()

        }

        markerArray.clear()
        nearByCabsArray.addAll(selectedCab!!.near)

        moveCabs()


        pickUpMarker?.apply {

            setPickUpMarker()
        }


    }


    override fun onCameraChange(p0: CameraPosition?) {
    }


    fun createBooking(tentativePrice: String) {

        ProgressDialog.showProgressBar(context!!, "", false)

        godViewModel.createBooking(
            selectedCab!!,
            LocationChildFragment.pickUpAddress.value!!,
            LocationChildFragment.dropOffAddress.value!!,
            tentativePrice
        )
    }


}