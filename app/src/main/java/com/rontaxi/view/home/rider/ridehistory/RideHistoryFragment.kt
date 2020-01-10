package com.rontaxi.view.home.rider.ridehistory

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getToken
import com.rontaxi.model.Booking
import com.rontaxi.model.DataRiderHistory
import com.rontaxi.model.drivingearning.DrivingEarningData
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.convertMintToHour
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.home.earnings.EarningsFragment
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.rider.HomeRiderActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.alert_no_booking.*
import kotlinx.android.synthetic.main.fragment_driver_earning_today.*
import kotlinx.android.synthetic.main.fragment_trip_history.*
import kotlinx.android.synthetic.main.fragment_trip_history.noBookingLayout
import kotlinx.android.synthetic.main.tool_bar_generic.*
import java.sql.Timestamp
import javax.inject.Inject

class RideHistoryFragment : BaseFragment(), RideHistoryAdapter.RideHistoryInterface {
    override fun onCancelRideClick(booking: Booking) {
        // implementation only for upcoming rides, not applicable here

    }

    lateinit var rideHistoryAdapter: RideHistoryAdapter


    var arrayListRiderHistory = ArrayList<Booking>()
    var itemLimit = 0
    var totalCount = 0

    override fun getLayoutRes() = R.layout.fragment_trip_history

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isLoaded) {
            return
        }


        initRecyclerView()

        onScroll()
    }

    private fun onScroll() {
        tripHistoryRecyclerView.addOnScrollListener(onScrollListener)
    }

    override fun onItemClick(position: Int) {
        val rideHistoryDetailFragment = RideHistoryDetailFragment()
        rideHistoryDetailFragment.bookingObj = arrayListRiderHistory[position]

        if (activity is HomeRiderActivity) {
            (activity as HomeRiderActivity).replaceFragment(rideHistoryDetailFragment, true)

        } else {
            (activity as HomeDriverActivity).replaceFragment(rideHistoryDetailFragment, true)
        }
    }

    private fun initRecyclerView() {
        rideHistoryAdapter = RideHistoryAdapter()
        tripHistoryRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        rideHistoryAdapter.rideHistoryInterface = this
        tripHistoryRecyclerView.adapter = rideHistoryAdapter
    }


    var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (!recyclerView.canScrollVertically(1)) {
                Log.e("lastPosition", "lastPosition")

                if (itemLimit < totalCount) {

                    (parentFragment as RidesContainerFragment).getRidesHistory()
                }
            }
        }

    }

    fun resetData() {
        itemLimit = 0
        totalCount = 0
        arrayListRiderHistory.clear()
        rideHistoryAdapter.notifyDataSetChanged()
    }


    fun drivingEarningData(data: DataRiderHistory) {


        itemLimit = itemLimit.plus(data.limit)
        totalCount = data.totalCount
        data.bookingObj?.let { it1 -> rideHistoryAdapter.setdata(it1) }
        rideHistoryAdapter.notifyDataSetChanged()
        data.bookingObj?.let { it1 -> arrayListRiderHistory.addAll(it1) }


        if (arrayListRiderHistory.isNotEmpty()) {
            tvNoBooking.text = getString(R.string.no_booking_data)
            noBookingLayout.visibility = View.GONE

        } else {
            tvNoBooking.text = getString(R.string.no_booking_data)
            noBookingLayout.visibility = View.VISIBLE
        }
    }

}