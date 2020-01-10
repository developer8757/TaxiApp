package com.rontaxi.view.home.driver.home.profile.updatephone

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.driver.otp.DriverOtpFragment
import com.rontaxi.view.initial.rider.otp.OtpFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_change_phone.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class ChangePhoneFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var updateDriverPhoneViewModel: ChangePhoneViewModel

    lateinit var phone: Phone


    override fun getLayoutRes() = R.layout.fragment_change_phone

    override fun showTitleBar() = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isLoaded) {
            return
        }

        setInformation()
        setObserver()
        clickListners()
        setToolbar()
    }


    private fun setInformation(){


        val user=updateDriverPhoneViewModel.getUserProfile()

        countryCodePicker.setCountryForPhoneCode(user.phone!!.code.replace("+","").toInt())
        etNewPhone.setText(user.phone!!.number)

    }

    private fun setObserver() {
        updateDriverPhoneViewModel.UpdateDriverPhoneLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                if (isResponseValid(activity!!, it.body()!!)) {
                    val succesData=it.body()!!

                    if(BuildConfig.ROLE==0){

                        var fragment = OtpFragment()
                        fragment.phone = phone
                        fragment.navigation = OtpFragment.NAVIGATION.CHANGE_PHONE
                        fragment.loginToken = succesData.data?.loginToken
                        // save temp access token in preferences
                        // updateDriverPhoneViewModel.saveToken()


                        (activity as HomeRiderActivity).replaceFragment(fragment, true)


                    }else {
                        var fragment = DriverOtpFragment()
                        fragment.phone = phone
                        fragment.navigation = DriverOtpFragment.NAVIGATION.CHANGE_PHONE
                        fragment.loginToken = succesData.data?.loginToken
                        // save temp access token in preferences
                        // updateDriverPhoneViewModel.saveToken()


                        (activity as HomeDriverActivity).replaceFragment(fragment, true)
                    }
                    Toasty.success(context!!, it.body()!!.message).show()
                }
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })


    }


    private fun setToolbar() {
        toolbarTitle.text = resources.getString(R.string.change_phone)
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun clickListners() {
        btnUpdatePhone.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v) {
            btnUpdatePhone -> {
                updatePhoneApi()
            }

        }
    }

    private fun updatePhoneApi() {
        if (Validations.isEmpty(context!!, etNewPhone)
            && Validations.isValidPhoneNumber(context!!, etNewPhone)
        ) {


            ProgressDialog.showProgressBar(context!!, getString(R.string.wait), false)

            phone = Phone()
            phone.number = etNewPhone.text.toString().trim()
            phone.code = countryCodePicker.selectedCountryCodeWithPlus

            updateDriverPhoneViewModel.changePhone(phone)


        }
    }

}