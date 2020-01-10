package com.rontaxi.view.home.driver.home

import android.arch.lifecycle.Observer
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.currentSocketState
import com.rontaxi.cache.isDriverOnline
import com.rontaxi.constants.getDummyRoute
import com.rontaxi.model.Booking
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.socket.SocketManager
import com.rontaxi.util.NotificationHelper
import com.rontaxi.util.cancelNotification
import com.rontaxi.util.getMarkerIconFromDrawable
import com.rontaxi.util.showAlert
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.driver.home.earnings.EarningsFragment
import com.rontaxi.view.home.driver.home.map.MapViewFragment
import com.rontaxi.view.home.driver.home.map.getSedanMarker
import com.rontaxi.view.home.driver.home.map.moveCabOnMap
import com.rontaxi.view.home.driver.home.profile.AccountFragment
import com.rontaxi.view.home.driver.home.rating.DriverRatingFragment
import com.rontaxi.view.home.rider.rate.RateDriverFragment
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.fragment_driver_home.*
import javax.inject.Inject

class HomeDriverFragment : BaseFragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        var LOCATION_REQUEST_CODE = 201
    }

    @Inject
    lateinit var homeDriverViewModel: HomeDriverViewModel

    var notifyDriverDialog: NotifyDriverDialog? = null


    val mapViewFragment = MapViewFragment()

    val accountFragment = AccountFragment()
    val driverRatingFragment = DriverRatingFragment()
    val earningsFragment: EarningsFragment by lazy {
        EarningsFragment()
    }

    val compDispPerm: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    val rxPermission: RxPermission by lazy {
        RealRxPermission.getInstance(activity!!.application)
    }


    override fun getLayoutRes() = R.layout.fragment_driver_home

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isLoaded) {
            return
        }

        bottom_nav_view_driver.setOnNavigationItemSelectedListener(this)
        replaceChildFragment(mapViewFragment)

        setObservers()


    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {

            R.id.action_home -> {

                replaceChildFragment(mapViewFragment)

            }
            R.id.action_earnings -> {
                replaceChildFragment(earningsFragment)
            }

            R.id.action_rating -> {
                replaceChildFragment(driverRatingFragment)
            }

            R.id.action_account -> {
                replaceChildFragment(accountFragment)
            }


        }
        return true
    }


    fun replaceChildFragment(fragment: Fragment) {

        childFragmentManager.beginTransaction()
            .replace(R.id.childFragmentContainer, fragment)
            .addToBackStack(fragment.javaClass.canonicalName)
            .commit()

    }

    fun setObservers() {


        isDriverOnline.observe(this, Observer {

            if (it!!) {

                getLocationsUpdate()

                homeDriverViewModel.startListeningNotifyDriver()

            }
        })



        setLocationUpdateObserver()


        /**
         * just for testing
         * dummy code to test route movement with animation and bearing
         */

        /*var dummyData: ArrayList<Pair<Double, Double>> = getDummyRoute()

        var position: Int = 0

        var handler = Handler()


        var runnable = object : Runnable {
            override fun run() {

                if (position < dummyData.size) {
                    var location: Location = Location("test");
                    location.latitude = dummyData.get(position).first
                    location.longitude = dummyData.get(position).second

                    mapViewFragment.moveCabOnMap(location)


                    position++
                    handler.postDelayed(this, 5000)


                }

            }
        }

        handler.post(runnable)
*/

        /**
         * uncomment below code when not doing testing with above dummy route
         */

        homeDriverViewModel.currentLocation.observe(this, Observer {

            mapViewFragment.moveCabOnMap(it!!)

        })

        homeDriverViewModel.notifyDriverLiveData.observe(this, Observer {


            it?.let {

                BookingManager.currentBooking = it.bookingObj


                    notifyDriverDialog?.dismiss()


                    notifyDriverDialog = NotifyDriverDialog(activity!!)


                    notifyDriverDialog!!.setData(it!!.bookingObj!!)

                    notifyDriverDialog!!.notifyDriverActionInterface =
                        object : NotifyDriverDialog.NotifyDriverActionInterface {
                            override fun onAcceptTapped() {

                                acceptBooking()
                            }

                            override fun onRejectTapped() {

                                rejectBooking()
                            }
                        }


                    notifyDriverDialog!!.show()

            }


        })



        homeDriverViewModel.acceptRejectRequestLiveData.observe(this, Observer {


            BookingManager.currentBooking = it!!.booking


            updateView(it.booking!!)

        })




        setUpdateBookingObserver()

        homeDriverViewModel.getBookingStatusLiveData.observe(this, Observer {


            it?.data?.bookingObj?.let {

                BookingManager.currentBooking = it

                updateView(it)


            }
        })


        homeDriverViewModel.bookingStatusUpdateLiveData.observe(
            this,
            Observer(function = {

                it?.data?.bookingObj?.let({
                    BookingManager.currentBooking = it

                    updateView(it)
                })
            })
        )

        homeDriverViewModel.startListeningBookingStatus()

    }


    fun updateView(booking: Booking) {


        if (!mapViewFragment.isVisible) {
            replaceChildFragment(mapViewFragment)

        }

        mapViewFragment.updateBookingStatus(booking)

        when (booking.bookingStatus) {

            0 -> {


                /*if (notifyDriverDialog != null) {
                    notifyDriverDialog!!.dismiss()
                }

                notifyDriverDialog = NotifyDriverDialog(activity!!)

                notifyDriverDialog!!.setData(booking)

                notifyDriverDialog!!.notifyDriverActionInterface =
                    object : NotifyDriverDialog.NotifyDriverActionInterface {
                        override fun onAcceptTapped() {

                            acceptBooking()
                        }

                        override fun onRejectTapped() {

                            rejectBooking()
                        }
                    }


                notifyDriverDialog!!.show()*/

                BookingManager.currentBooking = null

                bottom_nav_view_driver.visibility = View.VISIBLE
            }

            1 -> {
                bottom_nav_view_driver.visibility = View.GONE

            }

            2 -> {

                bottom_nav_view_driver.visibility = View.GONE

            }

            3 -> {


                bottom_nav_view_driver.visibility = View.VISIBLE

                val frag = RateDriverFragment()
                frag.booking = BookingManager.currentBooking!!

                (activity as HomeDriverActivity).replaceFragment(frag, true)
                BookingManager.currentBooking = null
            }

            4 -> {

                bottom_nav_view_driver.visibility = View.GONE

            }

            5 -> {

                bottom_nav_view_driver.visibility = View.GONE

            }


            6 -> {


                notifyDriverDialog?.dismiss()


                BookingManager.currentBooking = null

                bottom_nav_view_driver.visibility = View.VISIBLE


                showAlert(
                    context = context!!,
                    message = getString(R.string.rider_cancelled_booking),
                    buttonText = getString(R.string.ok),
                    onClick = {


                    })

            }


            7 -> {

                BookingManager.currentBooking = null

                bottom_nav_view_driver.visibility = View.VISIBLE

                showAlert(
                    context = context!!,
                    message = getString(R.string.booking_has_been_cancelled),
                    buttonText = getString(R.string.ok),
                    onClick = {


                    })


            }

        }


    }


}