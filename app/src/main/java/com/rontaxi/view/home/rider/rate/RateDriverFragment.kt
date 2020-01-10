package com.rontaxi.view.home.rider.rate

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.RonTaxiApp
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getUser
import com.rontaxi.model.Booking
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.addtip.AddTipFragment
import com.rontaxi.view.home.rider.payment.PaymentFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_rate_driver.*
import javax.inject.Inject

class RateDriverFragment : BaseFragment() {

    lateinit var booking: Booking

    @Inject
    lateinit var rateDriverViewModel: RateDriverViewModel

    override fun getLayoutRes() = R.layout.fragment_rate_driver

    override fun showTitleBar() = false

    var fromDetails=false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isLoaded)
            return

        tvFareAmount.text = booking.displayAmount
        tvPickUpAddress.text = booking.pickupAddress
        tvDropUpAddress.text = booking.dropAddress
        ratingRiderDetail.rating = booking.rating.toFloat()
        etComments.setText(booking.comment)

        ibBack.setOnClickListener {
            (activity!!).onBackPressed()
        }

        /**
         * clear notification if rider is on the rating screen already
         */
        cancelNotification(RonTaxiApp.context)


        rateDriverViewModel.ratingLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()

            if (it!!.isSuccessful) {


                booking.rating = ratingRiderDetail.rating.toInt()
                booking.comment = etComments.text.toString().trim()


                Toasty.success(context!!, it.body()!!.message).show()

                (activity)!!.supportFragmentManager.popBackStackImmediate()


            } else {

                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }


        })


        btnRate.setOnClickListener {

            hideKeyBoard(context!!, it)

            ProgressDialog.showProgressBar(context!!, "", false)
            rateDriverViewModel.rateDriver(
                ratingRiderDetail.rating.toInt(),
                booking.bookingId,
                etComments.text.toString().trim()
            )
        }

       /* btnHelp.setOnClickListener {

            hideKeyBoard(context!!, it)

            val frag = HelpFragment()
            frag.isRaiseIssue = true
            frag.bookingId = booking.bookingId

            if (activity is HomeRiderActivity) {
                (activity as HomeRiderActivity).replaceFragment(frag, true)
            } else {
                (activity as HomeDriverActivity).replaceFragment(frag, true)
            }
        }*/

        if(BuildConfig.ROLE==0 && !fromDetails){
            btnAddTip.visibility= View.VISIBLE
        }else{
            btnAddTip.visibility= View.GONE
        }

        btnAddTip.setOnClickListener{


            var user= getUser(context!!)

            if(user?.payment!!.isNotEmpty()){

                var paymentMethod=user?.payment!!.singleOrNull{

                    it.isSelected
                }

                if(paymentMethod!=null){

                    if(paymentMethod.type!!.toLowerCase().trim().equals("cash")){
                        showAlertWithCancel(context!!,
                            getString(R.string.slct_online_payment_for_tip),
                            getString(R.string.ok),
                            getString(R.string.cancel),
                            {
                                moveToPayments()

                            },
                            {})

                    }else{

                        val addTipFragment = AddTipFragment()
                        addTipFragment.receiverId= booking.driverObj?.userId!!
                        addTipFragment.bookingId=booking.bookingId
                        (activity as HomeRiderActivity).replaceFragment(addTipFragment, true)



                    }

                }else{

                    showAlertWithCancel(context!!,
                        getString(R.string.slct_online_payment_for_tip),
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        {
                            moveToPayments()

                        },
                        {})

                }
            }else{
                showAlertWithCancel(context!!,
                    getString(R.string.slct_online_payment_for_tip),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    {
                        moveToPayments()

                    },
                    {})

            }

        }
    }



    private fun moveToPayments(){

        (activity as HomeRiderActivity).replaceFragment(PaymentFragment(),true)
    }

}