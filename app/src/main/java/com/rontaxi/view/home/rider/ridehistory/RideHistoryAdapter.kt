package com.rontaxi.view.home.rider.ridehistory

import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.constants.BookingStatus
import com.rontaxi.model.Booking
import kotlinx.android.synthetic.main.ride_trip_history.view.*
import java.sql.Timestamp
import java.util.*

class RideHistoryAdapter : RecyclerView.Adapter<RideHistoryAdapter.Holder>() {

    var data = ArrayList<Booking>()

    var paymentDoneType = ""

    lateinit var rideHistoryInterface: RideHistoryInterface

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.ride_trip_history,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bind(p1)
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            itemView.apply {


                if (RidesContainerFragment.selectedTabPosition == 1) {
                    group_upcomingRides.visibility = View.VISIBLE
                } else {
                    group_upcomingRides.visibility = View.GONE

                }



                when (data[position].bookingStatus) {
                    BookingStatus.PENDING.value, BookingStatus.ONTHEWAY.value, BookingStatus.COMPLETED.value, BookingStatus.ARRIVED.value, BookingStatus.STARTED.value -> {
                        paymentTypeTV.setTextColor(resources.getColor(R.color.grey_shade3))
                        if (data[position].paymentMethod == 0)
                            paymentDoneType = context!!.getString(R.string.cash_payment_type)
                        else
                            paymentDoneType = context!!.getString(R.string.card_payment_type)
                        paymentTypeTV.text = paymentDoneType
                    }
                    BookingStatus.CANCELLEDBYCUSTOMER.value, BookingStatus.CANCELLEDBYDRIVER.value -> {
                        paymentTypeTV.setTextColor(resources.getColor(R.color.cancelled_red_shade))
                        paymentTypeTV.text = context!!.getString(R.string.cancelled)
                    }
                }
                val timestamp = data[position].createdAt
                val ts = Timestamp(timestamp.toLong())
                val date = DateFormat.format("dd-MM-yyyy HH:mm", ts)
                if (RidesContainerFragment.selectedTabPosition == 1) {
                    carBrandNameTV.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.sp_12)
                    );

                    val timestamp = data[position].scheduledDate
                    val ts = Timestamp(timestamp.toLong())
                    val date = DateFormat.format("dd-MM-yyyy HH:mm", ts)

                    carBrandNameTV.text = date
                } else {
                    carBrandNameTV.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.sp_16)
                    );

                    if (BuildConfig.ROLE == 0) {
                        carBrandNameTV.text =
                            "${data[position].driverObj?.brand}  ${data[position].driverObj?.model}"

                    } else {
                        carBrandNameTV.text =
                            "${data[position].riderObj?.firstName}  ${data[position].riderObj?.lastName}"

                    }
                }
                tripDateTimeTV.text = date
                startAddressDetailTV.text = data[position].pickupAddress
                endAddressDetailTV.text = data[position].dropAddress
                tripPriceTV.text = data[position].tentativeBudget.toUpperCase()
                setOnClickListener {
                    rideHistoryInterface.onItemClick(position)
                }

                btnCancelRide.setOnClickListener {

                    rideHistoryInterface.onCancelRideClick(data[position])

                }
            }
        }
    }

    interface RideHistoryInterface {
        fun onItemClick(position: Int)
        fun onCancelRideClick(booking: Booking)
    }

    fun setdata(list: ArrayList<Booking>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }
}