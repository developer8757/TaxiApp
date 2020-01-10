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
import com.rontaxi.model.ReasonModel
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.showAlert
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.godview.CancellationReasonsDialog
import kotlinx.android.synthetic.main.alert_no_booking.*
import kotlinx.android.synthetic.main.fragment_trip_history.*
import kotlinx.android.synthetic.main.fragment_trip_history.noBookingLayout
import javax.inject.Inject

class UpcomingRidesFragment : BaseFragment(), RideHistoryAdapter.RideHistoryInterface {


    @Inject
    lateinit var upcomingRidesViewModel: UpcomingRidesViewModel
    lateinit var rideHistoryAdapter: RideHistoryAdapter
    var arrayListRiderHistory = ArrayList<Booking>()
    var itemLimit = 0
    var totalCount = 0
    var cancellationDialog: CancellationReasonsDialog? = null

    var cancellationBookingId = ""


    override fun getLayoutRes() = R.layout.fragment_trip_history

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isLoaded) {
            return
        }

        setObservers()
        //   setToolBar()
        initRecyclerView()
        onScroll()
    }


    private fun onScroll() {
        tripHistoryRecyclerView.addOnScrollListener(onScrollListener)
    }

    override fun onItemClick(position: Int) {
//        val rideHistoryDetailFragment = RideHistoryDetailFragment()
//        rideHistoryDetailFragment.bookingObj = arrayListRiderHistory[position]
//        (activity as HomeRiderActivity).replaceFragment(rideHistoryDetailFragment, true)
    }

    override fun onCancelRideClick(booking: Booking) {

        cancellationBookingId = booking.bookingId;
        getCancellationReasons()
    }


    fun getCancellationReasons() {


        if (upcomingRidesViewModel.getCancellationReasonsLiveData.value != null) {

            showCancellationDialog(
                upcomingRidesViewModel.getCancellationReasonsLiveData.value?.body()?.data!!
            )

        } else {


            ProgressDialog.showProgressBar(context!!, "", false)
            upcomingRidesViewModel.getCancellationReasons()
        }


    }

    fun showCancellationDialog(data: ArrayList<ReasonModel>) {

        cancellationDialog = CancellationReasonsDialog(context!!)
        cancellationDialog!!.reasonsArrayList.addAll(data)
        cancellationDialog!!.cancellationDialogInterface =
            object : CancellationReasonsDialog.CancellationDialogInterface {

                override fun onDonePressed(reasonModel: ReasonModel) {

                    upcomingRidesViewModel.cancelBooking(
                        cancellationBookingId,
                        reasonModel.reasonId
                    )
                }


            }

        cancellationDialog!!.show()


    }

    fun setObservers() {

        upcomingRidesViewModel.getCancellationReasonsLiveData.observe(this, Observer {


            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                showCancellationDialog(it.body()?.data!!)


            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })

        upcomingRidesViewModel.updateBookingStatusLiveData.observe(this, Observer {

            it?.data?.bookingObj?.let {

                if (it.bookingStatus == 6) {
                    showAlert(
                        context!!,
                        getString(R.string.booking_has_been_cancelled),
                        getString(R.string.ok),
                        {})

                    resetData()
                }
            }
        })
    }

    fun resetData() {
        itemLimit = 0
        totalCount = 0
        arrayListRiderHistory.clear()
        rideHistoryAdapter.notifyDataSetChanged()
    }


    private fun initRecyclerView() {
        rideHistoryAdapter = RideHistoryAdapter()
        tripHistoryRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        rideHistoryAdapter.rideHistoryInterface = this
        tripHistoryRecyclerView.adapter = rideHistoryAdapter
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
}