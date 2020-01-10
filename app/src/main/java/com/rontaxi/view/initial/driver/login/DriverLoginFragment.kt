package com.rontaxi.view.initial.driver.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.driver.home.payment.BankDetailFragment
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.forgotpassword.DriverForgotPasswordFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.tool_bar_generic.*
import kotlinx.android.synthetic.main.fragment_driver_login.*

import javax.inject.Inject

class DriverLoginFragment : BaseFragment() {

    @Inject
    lateinit var driverLoginViewModel: DriverLoginViewModel

    override fun getLayoutRes() = R.layout.fragment_driver_login

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isLoaded) {
            return
        }
        setToolbar()
        setClicks()
        setObserver()
    }

    private fun setObserver() {
        driverLoginViewModel.loginLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {
                if (isResponseValid(activity!!, it.body()!!)) {

                    driverLoginViewModel.saveUser()
                    driverLoginViewModel.saveToken()
                    driverLoginViewModel.connectSockets()

                    when (it.body()!!.data!!.userObj!!.isAccountAdded) {


// no driver account added yet
                        0 -> {

                            (activity as InitialDriverActivity).replaceFragment(
                                BankDetailFragment(),
                                true
                            )

                        }

// driver account is added, not verified
                        1 -> {

                            (activity as InitialDriverActivity).replaceFragment(
                                BankDetailFragment(),
                                true
                            )


                        }

// driver account is added and verified
                        2 -> {

                            Toasty.success(context!!, it.body()!!.message).show()
                            startActivity(Intent(activity, HomeDriverActivity::class.java))
                            (activity!!).finish()

                        }
                    }

                }

            } else {
                showAlert(
                    context!!,
                    getErrorMessage(it.errorBody()!!).message,
                    getString(R.string.ok),
                    {})
                // showErrorMessage(context!!, getErrorMessage(it.errorBody()!!).message)
            }

        })
    }

    private fun setToolbar() {

        val textTitle = TextView(context)

        textTitle.text = resources.getString(R.string.login)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen.sp_20)
        )

        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llMiddle.gravity = (Gravity.START or Gravity.CENTER_VERTICAL)

        llEnd.removeAllViews()
        // llEnd.addView(ibFilter)
        // llEnd.addView(ibBell)


        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolBarGeneric.navigationIcon?.setTint(ContextCompat.getColor(activity!!, R.color.white))
        toolBarGeneric.setNavigationOnClickListener {
            (activity as InitialDriverActivity).onBackPressed()
        }

    }

    private fun setClicks() {

        btnLogin.setOnClickListener {


            if (InternetCheck.isConnectedToInternet(context!!)
                && Validations.isValidPhoneNumber(context!!, etPhone)
                && Validations.isEmpty(context!!, etPassword)
            ) {

                val loginRequestModel = LoginRequestModel()
                loginRequestModel.phone.number = etPhone.text.toString().trim()
                loginRequestModel.phone.code = "+${countryCodePicker.selectedCountryCode}"

                loginRequestModel.password = etPassword.text.toString().trim()


                hideKeyBoard(context!!, view!!)


                ProgressDialog.showProgressBar(context!!, getString(R.string.loging_in), false)
                driverLoginViewModel.login(loginRequestModel)
            }

        }

        tvForgotPassword.setOnClickListener {
            (activity as InitialDriverActivity).replaceFragment(
                DriverForgotPasswordFragment(),
                true
            )
        }
    }

}