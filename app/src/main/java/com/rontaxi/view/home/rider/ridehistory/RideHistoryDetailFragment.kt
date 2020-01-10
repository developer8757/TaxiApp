package com.rontaxi.view.home.rider.ridehistory

import android.os.Bundle
import android.text.format.DateFormat
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.rontaxi.R
import com.rontaxi.constants.BookingStatus
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.loadProfileImageFromURL
import android.arch.lifecycle.Observer
import android.view.MotionEvent
import com.rontaxi.BuildConfig
import com.rontaxi.base.BaseFragment
import com.rontaxi.model.*
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.godview.CancellationReasonsDialog
import com.rontaxi.view.home.rider.help.HelpFragment
import com.rontaxi.view.home.rider.rate.RateDriverFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_ride_history_detail.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import java.util.*
import javax.inject.Inject

class RideHistoryDetailFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var viewModel: RideHistoryDetailViewModel
    lateinit var bookingObj: Booking
    var selectedReasonModel: ReasonModel? = null
    var blockDriverModel = BlockDriverModel()

    val dialogReason: CancellationReasonsDialog by lazy {
        CancellationReasonsDialog(context!!)
    }
    var paymentDoneType = ""

    override fun getLayoutRes() = R.layout.fragment_ride_history_detail

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolBar()
        setOnClickEvents()
        addObserver()

        if (BuildConfig.ROLE == 1) {
            // if driver hide rider views
            groupRiderViews.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()

        setTheRideHistoryDetails()

    }

    private fun addObserver() {
        viewModel.helpReasonsLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {
                showCancellationDialog(it.body()!!.data!!)
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })

        viewModel.blockDriverLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                bookingObj.driverObj?.isBlocked = true

                btnBlockDriver.text = getString(R.string.blocked)
                Toasty.success(context!!, "Success").show()
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }

        })
    }

    private fun setOnClickEvents() {
        btnRaiseAnIssue.setOnClickListener(this)
        btnBlockDriver.setOnClickListener(this)

        ratingRiderDetail.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                var driverRatingFragment = RateDriverFragment()
                driverRatingFragment.booking = bookingObj

                if (activity is HomeRiderActivity) {

                    (activity as HomeRiderActivity).replaceFragment(driverRatingFragment, true)

                } else {
                    (activity as HomeDriverActivity).replaceFragment(driverRatingFragment, true)

                }

                return false
            }
        })

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnRaiseAnIssue -> {
                getRaiseIssues()
            }
            R.id.btnBlockDriver -> {
                getBlockReasons()

            }


        }
    }

    private fun getFormattedDate(smsTimeInMilis: Long): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = smsTimeInMilis
        val now = Calendar.getInstance()
        val timeFormatString = "hh:mm a"
        val dateTimeFormatString = "dd-MM-yyyy hh:mm a"
        if (now.get(Calendar.DATE) === smsTime.get(Calendar.DATE)) {
            return getString(R.string.todayat) + DateFormat.format(timeFormatString, smsTime)
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) === 1) {
            return getString(R.string.yesterdayat) + DateFormat.format(timeFormatString, smsTime)
        } else if (now.get(Calendar.YEAR) === smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            return DateFormat.format("dd-MM-yyyy hh:mm a", smsTime).toString()
        }
    }

    private fun showCancellationDialog(data: ArrayList<ReasonModel>) {

        dialogReason.reasonsArrayList.clear()
        dialogReason.reasonsArrayList.addAll(data)
        dialogReason.cancellationDialogInterface =
            object : CancellationReasonsDialog.CancellationDialogInterface {
                override fun onDonePressed(reasonModel: ReasonModel) {
                    selectedReasonModel = reasonModel

                    blockDriverModel.driverId = bookingObj.driverObj?.userId
                    blockDriverModel.bookingId = bookingObj.bookingId
                    blockDriverModel.reasonId = reasonModel.reasonId
                    blockDriverModel.comment = reasonModel.reason

                    viewModel.getBlockDriverReasons(blockDriverModel)
                    dialogReason.cancel()
                }
            }
        dialogReason.show()

    }

    private fun getBlockReasons() {
        if (viewModel.helpReasonsLiveData.value != null) {

            showCancellationDialog(viewModel.helpReasonsLiveData.value!!.body()!!.data!!)
        } else {
            ProgressDialog.showProgressBar(context!!, "", false)
            viewModel.getHelpReasons(4)
        }
    }

    private fun getRaiseIssues() {
        val helpFragment = HelpFragment()
        helpFragment.isRaiseIssue = true
        helpFragment.bookingId = bookingObj.bookingId
        if (activity is HomeRiderActivity)
            (activity as HomeRiderActivity).replaceFragment(helpFragment, true)
        else if (activity is HomeDriverActivity)
            (activity as HomeDriverActivity).replaceFragment(helpFragment, true)

    }

    private fun setTheRideHistoryDetails() {


        // show rating view only if booking status is completed
        groupCompletedRidesViews.visibility = View.GONE


        if (BuildConfig.ROLE == 0) {
            nameTV?.text = bookingObj.driverObj?.firstName + " " + bookingObj.driverObj?.lastName
        } else {
            nameTV?.text = bookingObj.riderObj?.firstName + " " + bookingObj.riderObj?.lastName

        }


        vehicleBrandTV?.text = bookingObj.driverObj?.brand + " " + bookingObj.driverObj?.model
        startAddressDetailTV?.text = bookingObj.pickupAddress
        endAddressDetailTV?.text = bookingObj.dropAddress
        tripPriceDetailTV?.text = bookingObj.displayAmount.toUpperCase()
        cabTypeTV?.text = bookingObj.driverObj?.carObj?.carType
        dateOfRideTV?.text = getFormattedDate(bookingObj.createdAt.toLong())
        fareCostTV?.text = bookingObj.displayAmount.toUpperCase()


        ratingRiderDetail?.rating = bookingObj.rating.toFloat()

        if (BuildConfig.ROLE == 0) {
            if (bookingObj.driverObj?.driverImage != null)
                profileImage?.loadProfileImageFromURL(context!!, bookingObj.driverObj!!.driverImage)
        } else {
            if (bookingObj.riderObj?.profileImage != null)
                profileImage?.loadProfileImageFromURL(context!!, bookingObj.riderObj!!.profileImage)
        }

        if (bookingObj.driverObj?.carObj?.image != null)
            bookingObj.driverObj!!.carObj?.image?.let {
                cabTypeImgCIV?.loadProfileImageFromURL(
                    context!!, it
                )
            }
        capacityCostTV?.text = "1 - ${bookingObj.driverObj?.carObj?.passengerCapacity}"
        distanceCostTV?.text = "${bookingObj.distace}km"
        if (bookingObj.paymentMethod == 0)
            paymentDoneType = context!!.getString(R.string.cash_payment_type)
        else
            paymentDoneType = context!!.getString(R.string.card_payment_type)
        when (bookingObj.bookingStatus) {
            BookingStatus.PENDING.value -> {
                setRideResultAndPayment(
                    getString(R.string.up_coming),
                    paymentDoneType,
                    resources.getColor(R.color.success_green_shade)
                )
            }
            BookingStatus.ONTHEWAY.value, BookingStatus.STARTED.value, BookingStatus.ARRIVED.value -> {
                setRideResultAndPayment(
                    getString(R.string.on_going),
                    paymentDoneType,
                    resources.getColor(R.color.success_green_shade)
                )
            }
            BookingStatus.COMPLETED.value -> {
                setRideResultAndPayment(
                    getString(R.string.completed),
                    paymentDoneType,
                    resources.getColor(R.color.success_green_shade)
                )

                groupCompletedRidesViews.visibility = View.VISIBLE
            }
            BookingStatus.CANCELLEDBYCUSTOMER.value, BookingStatus.CANCELLEDBYDRIVER.value -> {
                setRideResultAndPayment(
                    getString(R.string.cancelled),
                    "",
                    resources.getColor(R.color.cancelled_red_shade)
                )
            }
        }

        if (bookingObj.driverObj?.isBlocked!!) {
            btnBlockDriver.text = getString(R.string.blocked)
        }

        if (bookingObj.driverObj?.userId.toString().isEmpty()) {
            btnBlockDriver.visibility = View.GONE

        }
    }

    private fun setRideResultAndPayment(
        rideResultValue: String,
        paymentTypeValue: String,
        color: Int
    ) {
        rideResult.setTextColor(color)
        rideResult.text = rideResultValue
        paymentTypeDetailTV.text = paymentTypeValue
    }

    private fun setToolBar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.tripshistory)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        //llMiddle.gravity =  Gravity.CENTER
        llEnd.removeAllViews()
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {


            (activity)!!.onBackPressed()
        }
    }
}

