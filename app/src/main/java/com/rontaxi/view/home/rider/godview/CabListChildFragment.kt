package com.rontaxi.view.home.rider.godview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rontaxi.R
import com.rontaxi.cache.getUser
import com.rontaxi.model.GetRouteEtaFairModel
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.util.parseDate
import com.rontaxi.util.roundUpTo2
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.payment.PaymentFragment
import com.rontaxi.view.home.rider.schedulebooking.ScheduleBookingFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.child_fragment_cabs_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_schedulebooking.*
import java.util.*
import kotlin.collections.ArrayList

class CabListChildFragment : Fragment(), CabListAdapter.CabListListener {


    var viewCabList: View? = null

    var cabAdapter = CabListAdapter()


    var cabDataArray = ArrayList<CabsDetails>()

    var tentativeFare = ""

    lateinit var selectedCabDetails: CabsDetails

    var avoidActivityAttached = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (viewCabList == null) {
            viewCabList = inflater.inflate(R.layout.child_fragment_cabs_list, container, false)
        }

        return viewCabList
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setPaymentDetails()


        if (avoidActivityAttached) {
            avoidActivityAttached = false
            return
        }

        if (parentFragment is GodViewFragment) {
            (parentFragment as GodViewFragment).ibBack.visibility = View.VISIBLE
            (parentFragment as GodViewFragment).ibDrawer.visibility = View.GONE
        } else {
            btnConfirmRide.setText(getString(R.string.bookyourcab))
        }



        setData()

        setCabAdapter()


        btnConfirmRide.setOnClickListener {


            var user = getUser(context!!)

            user?.apply {
                if (this.payment.isNullOrEmpty()) {

                    avoidActivityAttached = true
                    Toasty.info(context!!, getString(R.string.add_payment_before_ride)).show()
                    (activity as HomeRiderActivity).replaceFragment(PaymentFragment(), true)
                    return@setOnClickListener
                } else {

                    var selectedPayment = this.payment!!.singleOrNull {
                        it.isSelected == true
                    }

                    if (selectedPayment == null) {

                        avoidActivityAttached = true
                        Toasty.info(context!!, getString(R.string.add_payment_before_ride)).show()
                        (activity as HomeRiderActivity).replaceFragment(PaymentFragment(), true)
                        return@setOnClickListener
                    }

                    if (parentFragment is GodViewFragment) {
                        (parentFragment as GodViewFragment).createBooking(tentativeFare)

                    } else {

                        (parentFragment as ScheduleBookingFragment).createBooking(
                            selectedCabDetails,
                            tentativeFare
                        )
                    }
                }
            }


        }

        tvPaymentMethod.setOnClickListener {

            avoidActivityAttached = true
            (activity as HomeRiderActivity).replaceFragment(PaymentFragment(), true)
        }


    }


    private fun setPaymentDetails() {


        var user = getUser(context!!)

        user?.apply {

            if (this.payment.isNullOrEmpty()) {


                tvPaymentMethod.text = getString(R.string.none)

            } else {
                var selectedPayment = this.payment!!.singleOrNull {
                    it.isSelected == true
                }

                if (selectedPayment == null) {
                    tvPaymentMethod.text = getString(R.string.none)
                    return
                }


                when (selectedPayment.type!!.toLowerCase()) {

                    "card" -> {
                        tvPaymentMethod.text = "******" + selectedPayment.last4

                    }

                    "cash" -> {
                        tvPaymentMethod.text = getString(R.string.cash)
                    }
                }

            }
        }


    }

    private fun setData() {


        if (cabDataArray.size > 0) {

            cabDataArray[0].isSelected = true
            setCabDetails(cabDataArray[0])
        }

    }


    private fun setCabDetails(cabObj: CabsDetails) {

        tvCabType.text = cabObj.carType
        tvCabDescription.text = cabObj.description
        tvPassengers.text = "1-${cabObj.passengerCapacity}"

    }

    private fun setCabAdapter() {


        rvCabs.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvCabs.adapter = cabAdapter
        cabAdapter.cabListListner = this
        cabAdapter.data = cabDataArray
        cabAdapter.notifyDataSetChanged()

    }

    fun setCabData(data: ArrayList<CabsDetails>) {
        cabAdapter.data.clear()
        cabAdapter.data = data
        cabAdapter.notifyDataSetChanged()
    }

    override fun onCabSelected(selectedCab: CabsDetails) {


        var selectedCabNew = cabDataArray.singleOrNull {

            selectedCab.carId.equals(it.carId)

        }

        selectedCabNew?.let {
            setCabDetails(it)
            tentativeFare = selectedCab.farePrice

            if (parentFragment is GodViewFragment) {
                (parentFragment as GodViewFragment).onSelectedCabTypeChanged(it)
            }
        }


    }


    fun updateFare(data: GetRouteEtaFairModel) {


        data.fare?.carFare?.forEach {

            val carFare = it
            val element = cabDataArray.single {

                it.carType.equals(carFare.name)
            }

            element.farePrice =
                "${data.fare!!.currency!!.unit}${roundUpTo2(carFare.fare * data.fare!!.currency!!.rate.toDouble())}"


        }

        cabAdapter.notifyDataSetChanged()

        tentativeFare = cabDataArray[0].farePrice
        selectedCabDetails = cabDataArray[0]


    }
}