package com.rontaxi.view.home.rider.schedulebooking

import android.app.DatePickerDialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.model.map.Address
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.getDate
import com.rontaxi.util.getTime
import com.rontaxi.util.parseDate
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.godview.CabListChildFragment
import com.rontaxi.view.home.rider.godview.GodViewFragment
import com.rontaxi.view.home.rider.godview.LocationChildFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_schedulebooking.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import java.util.*
import javax.inject.Inject

class ScheduleBookingFragment : BaseFragment() {


    @Inject
    lateinit var scheduleBookingViewModel: ScheduleBookingViewModel

    override fun getLayoutRes() = R.layout.fragment_schedulebooking

    override fun showTitleBar() = false

    lateinit var picker: DatePickerDialog


    val locationChildFragment: LocationChildFragment by lazy {
        LocationChildFragment()
    }

    val cabListChildFragment: CabListChildFragment by lazy {

        CabListChildFragment()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setToolBar()

        populateChildFragment(locationChildFragment, R.id.childContainer)

        setObservers()

        cabListChildFragment.cabDataArray = GodViewFragment.cabListChildFragment.cabDataArray


        getDate(tv_date, context!!)


        getTime(tv_time, context!!)

    }


    fun populateChildFragment(fragment: Fragment, container: Int) {

        childFragmentManager.beginTransaction()
            .replace(container, fragment, fragment::class.java.canonicalName).commit()
    }

    fun addCabListFragment() {
        populateChildFragment(cabListChildFragment, R.id.cabsContainer)

    }


    private fun setToolBar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.schedulecab)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        //llMiddle.gravity =  Gravity.CENTER
        llEnd.removeAllViews()
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            (activity as HomeRiderActivity).onBackPressed()
        }
    }

    fun getRouteAndEta() {


        scheduleBookingViewModel.getFairWithEta(
            LocationChildFragment.pickUpAddress.value!!,
            LocationChildFragment.dropOffAddress.value!!
        )


    }

    private fun setObservers() {

        scheduleBookingViewModel.getRoutesWithFairLiveData.observe(this, Observer {

            addCabListFragment()

            it?.let { it1 -> cabListChildFragment.updateFare(it1) }


        })

        scheduleBookingViewModel.scheduleBookingLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {
                if (isResponseValid(activity!!, it.body()!!)) {

                    (activity as HomeRiderActivity).setUserInformation()

                    Toasty.success(context!!, it.message()).show()


                    locationChildFragment.resetValues()

                    activity!!.onBackPressed()

                }
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }


        })

    }

    fun createBooking(selectedCab: CabsDetails, tentativePrice: String) {

        if (tv_date.text.toString().length > 0 && tv_time.text.toString().length > 0) {
            var scheduledDate = parseDate(
                tv_date.text.toString() + " " + (tv_time.text.toString().split("-")[0]),
                "dd.MM.yyyy HH:mm"
            )


            if (scheduledDate != null) {
                ProgressDialog.showProgressBar(context!!, "", false)

                scheduleBookingViewModel.createScheduleBooking(
                    selectedCab!!,
                    LocationChildFragment.pickUpAddress.value!!,
                    LocationChildFragment.dropOffAddress.value!!,
                    tentativePrice,
                    scheduledDate
                )
            }
        } else {
            Toasty.error(
                context!!,
                "Select both date and time for scheduled Ride",
                Toast.LENGTH_SHORT,
                true
            ).show()
        }


    }


}