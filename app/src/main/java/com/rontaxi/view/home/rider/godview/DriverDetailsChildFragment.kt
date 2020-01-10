package com.rontaxi.view.home.rider.godview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.model.Booking
import com.rontaxi.util.loadProfileImageFromURL
import com.rontaxi.util.makeTelephoneCall
import kotlinx.android.synthetic.main.child_fragment_driver_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class DriverDetailsChildFragment: Fragment() {


    var viewDriverDetails: View?=null

    lateinit var bookingModel: Booking



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(viewDriverDetails==null) {

            viewDriverDetails = inflater.inflate(R.layout.child_fragment_driver_details, container, false)
        }

        return viewDriverDetails
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (parentFragment as GodViewFragment).ibBack.visibility=View.GONE
        (parentFragment as GodViewFragment).ibDrawer.visibility=View.GONE

        tvDriverName.text="${bookingModel.driverObj?.firstName} ${bookingModel.driverObj?.lastName}"

        rbDriverRating.rating=bookingModel.driverObj?.rating!!.toFloat()

        tvDriverContact.text="${bookingModel.driverObj?.phone!!.code}-${bookingModel.driverObj?.phone!!.number}"

        tvCarType.text=bookingModel.driverObj?.carObj?.name

        tvCarNumber.text="${bookingModel.driverObj?.numberPlate} ${bookingModel.driverObj?.brand} ${bookingModel.driverObj?.model}"

        ivDriverImage.loadProfileImageFromURL(context!!,bookingModel.driverObj?.driverImage!!)

        updateBookingStatus(bookingModel)


        btnCallDriver.setOnClickListener {

            makeTelephoneCall(context!!,"${bookingModel.driverObj?.phone!!.code}${bookingModel.driverObj?.phone!!.number}")

        }


        btnCancelRide.setOnClickListener {
            (parentFragment as GodViewFragment).getCancellationReasons()
        }

    }



    fun setBookingDetails(booking: Booking?){


        bookingModel=booking!!

        updateBookingStatus(bookingModel)

    }

    fun updateBookingStatus(bookingModel: Booking){

        try {
            when (bookingModel.bookingStatus) {

                1 -> {
                    tvBookingStatus.text = "Your ride has been accepted"


                }

                2 -> {
                    tvBookingStatus.text = "Your driver is on the way"
                }

                4 -> {
                    tvBookingStatus.text ="Your driver has arrived"
                }

                5 -> {


                    tvBookingStatus.text = "Your ride has been started"

                }

                else -> {
                    tvBookingStatus.text = ""

                }


            }
        }catch (e: Exception){

        }
    }

}