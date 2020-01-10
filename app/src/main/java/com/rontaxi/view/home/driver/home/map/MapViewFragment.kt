package com.rontaxi.view.home.driver.home.map

import `in`.shadowfax.proswipebutton.ProSwipeButton
import android.arch.lifecycle.Observer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.MotionEvent
import android.view.View
import com.rontaxi.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.rontaxi.R
import com.rontaxi.cache.getDriverSavedOnlineStatus
import com.rontaxi.cache.isDriverOnline
import com.rontaxi.model.Booking
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.util.getMarkerIconFromDrawable
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.loadProfileImageFromURL
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.util.makeTelephoneCall
import com.rontaxi.util.showAlertWithCancel
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.home.HomeDriverFragment
import com.rontaxi.view.home.driver.home.getCurrentLocation
import com.rontaxi.view.home.driver.home.getLocationsUpdate
import com.rontaxi.view.home.rider.godview.CancellationReasonsDialog
import com.rontaxi.view.home.rider.godview.showCancellationDialog
import kotlinx.android.synthetic.main.fragment_map_view.*
import javax.inject.Inject

class MapViewFragment : BaseFragment(),
    OnMapReadyCallback/*, CompoundButton.OnCheckedChangeListener*/ {


    var myLocationMarker: Marker? = null

    var sedanMarker: BitmapDescriptor? = null

    var polyLine: Polyline? = null

    var pickUpMarker: Marker? = null
    var dropOffMarker: Marker? = null

    var customInfoWindowPickUpView: View? = null

    var cancellationDialog: CancellationReasonsDialog? = null


    private val previousZoomLevel = -1.0f


    @Inject
    lateinit var mapViewModel: MapViewModel

    lateinit var mMap: GoogleMap

    val supportMapFragment: SupportMapFragment by lazy {

        SupportMapFragment()
    }

    override fun getLayoutRes() = R.layout.fragment_map_view

    override fun showTitleBar() = false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if (isLoaded) {
            return
        }


        initMap()
        setObserver()

        if (isLoaded) {

            /* if(isDriverOnline.value!!){

                 (parentFragment as HomeDriverFragment).getCurrentLocation()
             }*/

            return
        }

        customInfoWindowPickUpView =
            activity!!.layoutInflater.inflate(R.layout.window_marker_pickup, null)

        isDriverOnline.postValue(false)


        setClick()


    }


    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!

        mapViewModel.getLastLocation()
        (parentFragment as HomeDriverFragment).getCurrentLocation()


        /**
         * resize marker size on zoom in and zoom out
         * alternatively we can use groundoverlays instead of marker to achieve same effect on zoom in and zoom out
         */

        mMap.setOnCameraMoveListener(GoogleMap.OnCameraMoveListener {
            val cameraPosition = mMap.getCameraPosition()


            if (previousZoomLevel != cameraPosition.zoom) {
//                myLocationMarker!!.setIcon(getSedanMarker(context!!))

                myLocationMarker.let {

                    it?.setIcon(
                        getMarkerIconFromDrawable(
                            context!!.getDrawable(R.drawable.sedan_marker_v),
                            cameraPosition.zoom
                        )
                    )


                }

            }

        })


    }


    fun initMap() {

        childFragmentManager
            .beginTransaction()
            .replace(R.id.map, supportMapFragment).commit()

        supportMapFragment.getMapAsync(this)


    }

    fun onPermissionGranted() {


        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = false

        try {
            mMap.isMyLocationEnabled = (true)

        } catch (ex: SecurityException) {

        }
    }

    fun setClick() {


        ibMyLocation.setOnClickListener {
            mapViewModel.getLastLocation()
        }


        switchStatus.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (event!!.action == MotionEvent.ACTION_UP) {
                    mapViewModel.changeDriverStatus(!isDriverOnline.value!!)

                    mapViewModel.saveDriverStatus(!isDriverOnline.value!!)
                    return true
                }



                return true
            }
        })


        btnSwipe.onSwipeListener = object : ProSwipeButton.OnSwipeListener {

            override fun onSwipeConfirm() {


                Handler().post(Runnable {


                    btnSwipe.showResultIcon(true, true)
                })


                when (BookingManager.currentBooking?.bookingStatus) {


                    1 -> {
                        mapViewModel.onWay()
                    }

                    2 -> {
                        mapViewModel.arrived()

                    }

                    4 -> {

                        mapViewModel.startTrip()
                    }
                    5 -> {

                        mapViewModel.completeTrip()
                    }


                }
            }
        }


        btnCallCustomer.setOnClickListener {
            if (BookingManager.currentBooking != null) {

                makeTelephoneCall(
                    context!!,
                    BookingManager.currentBooking?.riderObj!!.phone!!.code + BookingManager.currentBooking?.riderObj!!.phone!!.number
                )
            }
        }


        btnCancel.setOnClickListener {

            if (BookingManager.currentBooking != null) {

                showAlertWithCancel(
                    context!!,
                    getString(R.string.cancel_trip_driver),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    {

                        getCancellationReasons()

                    },
                    {})


            }
        }


        //switchStatus.setOnCheckedChangeListener(this)
    }

    fun setObserver() {


        mapViewModel.currentLocation.observe(this, Observer {

            it?.let {
                val currentLocation = LatLng(it.latitude, it.longitude)


                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLocation,
                        mMap.maxZoomLevel - 3
                    )
                )


            }

        })


        isDriverOnline.observe(this, Observer {

            /**
             * if status from socket is offline due to socket disconnect
             * and driver status was online
             * then change driver status to online again automatically on socket connect
             */

            if (!it!! && getDriverSavedOnlineStatus(context!!)) {

                mapViewModel.changeDriverStatus(!isDriverOnline.value!!)


            } else {
                switchStatus.isChecked = it!!

                if (it) {
                    tvStatus.setText(getString(R.string.you_are_online))
                } else {
                    tvStatus.setText(getString(R.string.you_are_offline))
                }
            }

        })

        mapViewModel.updateBookingStatusLiveData.observe(this, Observer {

            it?.data?.bookingObj?.let {

                BookingManager.currentBooking = it

                (parentFragment as HomeDriverFragment).updateView(it)


            }


        })

        mapViewModel.getCancellationReasonsLiveData.observe(this, Observer {


            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                showCancellationDialog(it.body()?.data!!)


            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }


        })


    }


    fun updateBookingStatus(booking: Booking) {


        dropPins(booking)

        clDriverAction?.visibility = View.VISIBLE

        tvPassengerName?.text = "${booking.riderObj!!.firstName} ${booking.riderObj!!.lastName}"

        ivPassenger?.loadProfileImageFromURL(context!!, booking.riderObj!!.profileImage)




        when (booking.bookingStatus) {

            0 -> {
                clDriverAction.visibility = View.GONE
                cvOnlineStatus.visibility = View.VISIBLE
                cvLocation.visibility = View.GONE
            }

            1 -> {
                clDriverAction?.visibility = View.VISIBLE
                cvOnlineStatus?.visibility = View.GONE
                cvLocation?.visibility = View.VISIBLE
                tvLocationTitle?.text = getString(R.string.pick_up_location_)
                tvLocation?.text = booking.pickupAddress

                btnSwipe?.text = resources.getString(R.string.on_way)
                btnSwipe?.backgroundColor = resources.getColor(R.color.colorMustard)

            }

            2 -> {
                clDriverAction?.visibility = View.VISIBLE
                cvOnlineStatus?.visibility = View.GONE
                cvLocation?.visibility = View.VISIBLE
                tvLocationTitle?.text = getString(R.string.pick_up_location_)
                tvLocation?.text = booking.pickupAddress
                //btnSwipe.showResultIcon(false)
                btnSwipe?.text = resources.getString(R.string.arrived)
                btnSwipe?.backgroundColor = resources.getColor(R.color.colorBlue)

            }

            4 -> {

                clDriverAction?.visibility = View.VISIBLE
                cvOnlineStatus?.visibility = View.GONE
                cvLocation?.visibility = View.VISIBLE
                tvLocationTitle?.text = getString(R.string.drop_off_location_)
                tvLocation?.text = booking.dropAddress
                //btnSwipe.showResultIcon(false)
                btnSwipe?.text = resources.getString(R.string.swipe_start)
                btnSwipe?.backgroundColor = resources.getColor(R.color.md_purple_700)

            }
            5 -> {

                clDriverAction?.visibility = View.VISIBLE
                cvOnlineStatus?.visibility = View.GONE
                cvLocation?.visibility = View.VISIBLE
                tvLocationTitle?.text = getString(R.string.drop_off_location_)

                tvLocation?.text = booking.dropAddress
                // btnSwipe.showResultIcon(false)
                btnSwipe?.text = getString(R.string.swipe_end_trip)
                btnSwipe?.backgroundColor = resources.getColor(R.color.md_green_500)

            }


            6 -> {

                cancellationDialog?.dismiss()
                clDriverAction?.visibility = View.GONE
                cvOnlineStatus?.visibility = View.VISIBLE
                cvLocation?.visibility = View.GONE

            }

            7 -> {

                cancellationDialog?.dismiss()
                clDriverAction?.visibility = View.GONE
                cvOnlineStatus?.visibility = View.VISIBLE
                cvLocation?.visibility = View.GONE


            }

            3 -> {

                clDriverAction?.visibility = View.GONE
                cvOnlineStatus?.visibility = View.VISIBLE
                cvLocation?.visibility = View.GONE

            }


        }


    }

    fun onReject() {


    }

    override fun onPause() {
        super.onPause()

        mMap.clear()
        myLocationMarker = null
    }

}


