package com.rontaxi.view.home.driver.home.earnings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.rontaxi.R
import com.rontaxi.model.drivingearning.DrivingEarningData
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.convertMintToHour
import com.rontaxi.view.home.driver.home.earnings.adapterearningdriver.DriverEarningAdapter
import kotlinx.android.synthetic.main.fragment_driver_earning_today.*
import javax.inject.Inject
import android.view.View
import com.rontaxi.base.BaseFragment
import com.rontaxi.model.drivingearning.DriverEarningBooking
import kotlinx.android.synthetic.main.alert_no_booking.*

class DriverEarningTodayFragment : BaseFragment() {
    @Inject
    lateinit var driverEarningTodayViewModel: DriverEarningTodayViewModel

    override fun getLayoutRes() = com.rontaxi.R.layout.fragment_driver_earning_today
    override fun showTitleBar() = false
    lateinit var driverEarningAdapter: DriverEarningAdapter
    var dataEarning = ArrayList<DriverEarningBooking>()


    var offset = 0
    // var totalLimit = 0
    var totalData = 0

    var listHeading = ""


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("onAct", "onAct")

        tvTodayTrip.text = listHeading
        setAdapter()
        onScroll()

    }


    private fun onScroll() {
        rvEarningToday.addOnScrollListener(onScrollListener)
    }

    private fun setAdapter() {
        rvEarningToday.layoutManager = LinearLayoutManager(activity)
        driverEarningAdapter = DriverEarningAdapter(activity!!)
        rvEarningToday.adapter = driverEarningAdapter
        driverEarningAdapter.data = dataEarning
    }

    fun drivingEarningData(data: DrivingEarningData) {

        data.booking?.let {
            dataEarning.addAll(data.booking)
        }


        totalData = data.totalCount

        offset = offset.plus(data.limit) + 1  // because index is starting from 0

        if (dataEarning.isNotEmpty()) {
            tvNoBooking.text = getString(R.string.no_booking_data)
            noBookingLayout.visibility = View.GONE
            tvTodayTrip.visibility = View.VISIBLE
            llEarning.visibility = View.VISIBLE

            tvDriverCompletedTrip.text = data.completedRides.toString()
            tvSpendTime.text = convertMintToHour(data.timeSpent)
            tvMyEarning.text = "${getString(R.string.currency)} ${data.totalEarning.displayAmount}"
        } else {
            tvNoBooking.text = getString(R.string.no_booking_data)
            noBookingLayout.visibility = View.VISIBLE
            tvTodayTrip.visibility = View.GONE
            llEarning.visibility = View.GONE
        }
        driverEarningAdapter.notifyDataSetChanged()
    }


    var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (!recyclerView.canScrollVertically(1)) {
                Log.e("lastPosition", "lastPosition")

                if (offset < totalData && !(parentFragment as EarningsFragment).isDataLoading) {
                    (parentFragment as EarningsFragment).isDataLoading = true

                    (parentFragment as EarningsFragment).callEarning()
                }
            }
        }

    }
}

