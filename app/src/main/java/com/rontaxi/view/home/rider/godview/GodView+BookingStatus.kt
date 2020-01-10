package com.rontaxi.view.home.rider.godview

import android.view.View
import com.rontaxi.R
import com.rontaxi.model.Booking
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.showAlert
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.rate.RateDriverFragment
import kotlinx.android.synthetic.main.fragment_home.*


fun GodViewFragment.updateView(booking: Booking){

    dropPins(booking)

    when(booking.bookingStatus){

        0->{
            ProgressDialog.hideProgressBar()

            searchDriverDialog?.dismiss()


           searchDriverDialog=SearchDriverDialog(context!!)
            searchDriverDialog!!.searchDriverDialogInterface= object:SearchDriverDialog.SearchDriverDialogInterface{
                override fun onCancelBooking() {
                    godViewModel.cancelBooking(null)
                }
            }
            searchDriverDialog!!.show()


        /*   BookingManager.currentBooking=null
            polyLine?.remove()
            pickUpMarker?.remove()
            dropOffMarker?.remove()
            LocationChildFragment.dropOffAddress.value=null
            pickUpMarker=null
            dropOffMarker=null

            selectedCab=null

            changeChildFragment(locationChildFragment)

            cancellationDialog?.dismiss()*/


           // btnSOS.visibility= View.GONE


        }



        1->{

            changeChildFragment(driverDetailsChildFragment)

            driverDetailsChildFragment.setBookingDetails(booking)
            btnSOS.visibility= View.GONE

        }

        2->{

            changeChildFragment(driverDetailsChildFragment)

            driverDetailsChildFragment.setBookingDetails(booking)
            btnSOS.visibility= View.GONE


        }

        3->{

            BookingManager.currentBooking=null
            polyLine?.remove()
            pickUpMarker?.remove()
            dropOffMarker?.remove()
            LocationChildFragment.dropOffAddress.value=null
            pickUpMarker=null
            dropOffMarker=null

            selectedCab=null

            changeChildFragment(locationChildFragment)


            var driverRatingFragment=RateDriverFragment()
                driverRatingFragment.booking=booking


            (activity as HomeRiderActivity).replaceFragment(driverRatingFragment,true)
            btnSOS.visibility= View.GONE

            cancellationDialog?.dismiss()

        }

        4->{

            changeChildFragment(driverDetailsChildFragment)

            driverDetailsChildFragment.setBookingDetails(booking)
            btnSOS.visibility= View.VISIBLE

        }


        5->{

            changeChildFragment(driverDetailsChildFragment)

            driverDetailsChildFragment.setBookingDetails(booking)

            btnSOS.visibility= View.VISIBLE

        }

        6->{


            BookingManager.currentBooking=null
            polyLine?.remove()
            pickUpMarker?.remove()
            dropOffMarker?.remove()
            LocationChildFragment.dropOffAddress.value=null
            pickUpMarker=null
            dropOffMarker=null

            selectedCab=null

            changeChildFragment(locationChildFragment)

            showAlert(context!!, getString(R.string.booking_has_been_cancelled), getString(R.string.ok),{})
            btnSOS.visibility= View.GONE

            cancellationDialog?.dismiss()

        }


        7->{

            BookingManager.currentBooking=null
            polyLine?.remove()
            pickUpMarker?.remove()
            dropOffMarker?.remove()
            LocationChildFragment.dropOffAddress.value=null
            pickUpMarker=null
            dropOffMarker=null

            selectedCab=null

            changeChildFragment(locationChildFragment)

            showAlert(context!!, getString(R.string.driver_cancelled_booking), getString(R.string.ok),{})
            btnSOS.visibility= View.GONE

            cancellationDialog?.dismiss()



        }


    }


}