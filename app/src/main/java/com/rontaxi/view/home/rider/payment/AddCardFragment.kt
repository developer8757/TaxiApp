package com.rontaxi.view.home.rider.payment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Token
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_card.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class AddCardFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var addCardViewModel: AddCardViewModel
    override fun getLayoutRes() = R.layout.fragment_add_card

    override fun showTitleBar() = false
    lateinit var stripe: Stripe

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initStripe()
        setOnClicks()
        addObserver()
        setToolBar()
    }

    private fun addObserver() {
        addCardViewModel.paymentLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()

            if(it!!.isSuccessful){

                (activity)!!.supportFragmentManager.popBackStackImmediate()
            }else{
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }



        })
    }

    private fun setOnClicks() {
        btnAdd.setOnClickListener(this)
    }


    private fun initStripe() {
        stripe = Stripe(context!!,(BuildConfig.STRIPE_KEY))
    }

    override fun onClick(p0: View?) {
        getCardToken()

    }

    private fun getCardToken() {
        val card = cardInputWidget.card

        if (card != null && card.validateCard()) {

            ProgressDialog.showProgressBar(context!!, "", false)
            stripe.createToken(card, object : ApiResultCallback<Token> {

                override fun onSuccess(result: Token) {

                    addCardViewModel.saveCardUsingToken(result.id)


                }

                override fun onError(e: Exception) {

                    Log.i("payment_token", "Fail to create token -> ${e.message}")
                }
            })

        }

    }

    private fun replaceFragment() {
        val paymentFragment = PaymentFragment()
            (activity as HomeRiderActivity).replaceFragment(paymentFragment, true)

    }

    private fun setToolBar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.add_new_card_detail)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llEnd.removeAllViews()
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolBarGeneric.setNavigationOnClickListener {
            (activity as HomeRiderActivity).onBackPressed()
        }
    }
}