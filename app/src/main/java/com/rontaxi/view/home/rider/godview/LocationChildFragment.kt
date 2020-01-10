package com.rontaxi.view.home.rider.godview

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.model.Booking
import com.rontaxi.model.map.Address
import com.rontaxi.util.formatDate
import com.rontaxi.util.parseDate
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.rate.RateDriverFragment
import com.rontaxi.view.home.rider.ridehistory.RidesContainerFragment
import com.rontaxi.view.home.rider.schedulebooking.ScheduleBookingFragment
import com.rontaxi.view.location.LocationActivity
import kotlinx.android.synthetic.main.child_fragment_location.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.sql.Timestamp

class LocationChildFragment : Fragment() {


    var viewLocation: View? = null


    companion object {

        var autoUpdatePickUp = true
        var pickUpAddress = MutableLiveData<Address>()

        var dropOffAddress=MutableLiveData<Address>()


        var PICK_UP_KEY="pickUpKey"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (viewLocation == null)
            viewLocation = inflater.inflate(R.layout.child_fragment_location, container, false)

        return viewLocation
    }

    fun resetValues() {
        autoUpdatePickUp = true
        pickUpAddress.value = null
        dropOffAddress.value = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if (parentFragment is GodViewFragment) {
            (parentFragment as GodViewFragment).ibBack.visibility = View.GONE
            (parentFragment as GodViewFragment).ibDrawer.visibility = View.VISIBLE
        } else {
            groupGodViewfragment.visibility = View.GONE
        }


        pickUpAddress.observe(this, Observer {

            it?.let {

                tvLocation.setText(it.address)
            }

        })

        dropOffAddress.observe(this, Observer {
            tvWhereTo.setText(getString(R.string.where_to))

            it?.let {


                tvWhereTo.setText(it.address)
            }


        })

        clPickUp.setOnClickListener {


            var intent=Intent(activity!!,LocationActivity::class.java)
                intent.putExtra(PICK_UP_KEY,PICK_UP_KEY)
            startActivityForResult(intent,LocationActivity.CURRENTLOCATION)
        }


        clDrop.setOnClickListener {

            startActivityForResult(
                Intent(activity!!, LocationActivity::class.java),
                LocationActivity.CURRENTLOCATION
            )

        }

        tvScheduleCab.setOnClickListener {

            if (GodViewFragment.upcomingBookings != null && GodViewFragment.upcomingBookings?.size!! > 0) {

                /**
                 * if schedule booking found navigate him to upcoming bookings fragment
                 */

                (activity as HomeRiderActivity).replaceFragment(RidesContainerFragment(), true)

            } else {

                var scheduleBookingFragment = ScheduleBookingFragment()

                (activity as HomeRiderActivity).replaceFragment(scheduleBookingFragment, true)
            }

        }

    }

    fun updateData(upComingBookings: ArrayList<Booking>?) {

        if (activity != null) {
            if (upComingBookings != null && upComingBookings.size > 0) {

                val timestamp = upComingBookings.get(0).scheduledDate
                val ts = Timestamp(timestamp.toLong())

                tvScheduleCab.setTextColor(resources.getColor(R.color.colorAccent))
                tvScheduleCab.text = "Scheduled On - " + formatDate(ts, "dd-MM-yyyy HH:mm")
            } else {

                tvScheduleCab.text = getString(R.string.schedule_your_cab)
            }
        }
    }


    fun updateLocation(location: String?, locality: String?) {

        /*if(!location.isNullOrEmpty()){
            tvLocation.text=location
        }else{
            tvLocation.text=""
        }

        if(!locality.isNullOrEmpty()){
            tvLocality.text=locality
        }else{
            tvLocality.text=""
        }*/
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode.equals(LocationActivity.CURRENTLOCATION)) {

            if (resultCode.equals(Activity.RESULT_OK)) {


                /*if(isValidAddress(pickUpAddress.value!!)){


                }*/


                if (isValidAddress(pickUpAddress.value!!) && isValidAddress(dropOffAddress.value!!)) {

                    if (parentFragment is GodViewFragment) {
                        (parentFragment as GodViewFragment).getRouteAndEta()

                    } else {
                        (parentFragment as ScheduleBookingFragment).getRouteAndEta()

                    }


                    //(parentFragment as GodViewFragment).changeChildFragment((parentFragment as GodViewFragment).cabListChildFragment)

                    //call next fragment
                }
            }

        }
    }


    private fun isValidAddress(address: Address): Boolean {

        if (address.lat != null && address.lng != null) {

            return true
        }
        return false
    }


}