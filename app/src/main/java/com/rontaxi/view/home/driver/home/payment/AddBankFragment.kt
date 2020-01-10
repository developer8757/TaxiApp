package com.rontaxi.view.home.driver.home.payment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.View
import com.rontaxi.base.BaseFragment
import com.rontaxi.util.ProgressDialog
import kotlinx.android.synthetic.main.fragment_add_bank.*
import javax.inject.Inject
import com.stripe.android.Stripe
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.Validations
import com.rontaxi.util.showAlert
import com.rontaxi.view.erroraction.tackleError
import com.stripe.android.ApiResultCallback
import com.stripe.android.model.BankAccount
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.tool_bar_generic.*

class AddBankFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var addBankViewModel: AddBankViewModel

    override fun getLayoutRes() = R.layout.fragment_add_bank

    override fun showTitleBar() = false
    lateinit var stripe: Stripe

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        initStripe()
        setOnClicks()
        addObserver()
        setToolBar()

    }

    private fun initStripe() {
        stripe = Stripe(context!!, (BuildConfig.STRIPE_KEY))
    }

    private fun addObserver() {
        addBankViewModel.addBankLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            it?.apply {

                if (isSuccessful) {
                    addBankViewModel.saveUser()

                    (activity!!).supportFragmentManager.popBackStackImmediate()



                } else {
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))
                }
            }
        })
    }

    private fun setOnClicks() {
        btnAddBank.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        val accountNumber = etAccNumber.text.toString()
        val routingNumber = etRoutingNumber.text.toString()
        val holderName = etAccHolderName.text.toString()
        val accType = etAccType.text.toString()

        if(Validations.isEmpty(context!!, etAccNumber)
            && Validations.isEmpty(context!!, etRoutingNumber)
            && Validations.isEmpty(context!!, etAccHolderName)
            && Validations.isEmpty(context!!, etAccType)
        ){

            generateToken(accountNumber, routingNumber, holderName, accType)
        }


    }

    private fun generateToken(accNo: String, routingNo: String, holderName: String, accType: String) {


        ProgressDialog.showProgressBar(context!!, "",false)
        val bankAccount = BankAccount(
            accNo, holderName, accType, null, "US",
            "usd", null, null, routingNo
        )

        stripe.createBankAccountToken(bankAccount, object : ApiResultCallback<Token> {

            override fun onSuccess(result: Token) {

                addBankViewModel.addBankAccount(result.id)

            }

            override fun onError(e: Exception) {

                ProgressDialog.hideProgressBar()

                showAlert(context!!, e.localizedMessage,getString(R.string.ok),{})

                Log.i("payment_token", "Success to create token -> $e")
            }
        })
    }

    private fun setToolBar() {
        toolbarTitle.text = resources.getString(R.string.add_bank_account)
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            activity!!.onBackPressed()

        }
    }

}