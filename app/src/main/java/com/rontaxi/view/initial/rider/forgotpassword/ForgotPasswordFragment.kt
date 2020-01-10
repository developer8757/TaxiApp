package com.rontaxi.view.initial.rider.forgotpassword

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.util.hideKeyBoard
import com.rontaxi.util.showAlert
import com.rontaxi.view.initial.rider.InitialRiderActivity
import com.rontaxi.view.initial.rider.otp.OtpFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import javax.inject.Inject

class ForgotPasswordFragment: BaseFragment() {



    lateinit var phone: Phone

    @Inject
    lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun getLayoutRes()=R.layout.fragment_forgot_password

    override fun showTitleBar()=false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if(isLoaded){
            return
        }

        setClicks()
        setObservers()

    }





    private fun setClicks(){

        btnSend.setOnClickListener {

            hideKeyBoard(context!!,view!!)

            if(Validations.isValidPhoneNumber(context!!, etPhone)){

                ProgressDialog.showProgressBar(context!!, getString(R.string.wait), false)


                     phone=Phone()
                    phone.number=etPhone.text.toString().trim()
                    phone.code="+${countryCodePicker.selectedCountryCode}"

                forgotPasswordViewModel.forgotPassword(phone)
            }

        }


    }

    private fun setObservers(){
        forgotPasswordViewModel.forgotPasswordLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()

            if(it!!.isSuccessful){


                Toasty.success(context!!,it.body()!!.message).show()


                var fragment=OtpFragment()
                fragment.phone=phone
                fragment.navigation=OtpFragment.NAVIGATION.FORGOT_PASSWORD


                // save temp access token in preferences
                forgotPasswordViewModel.saveToken()

                (activity as InitialRiderActivity).replaceFragment(fragment,false)


            }else{

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})

            }



        })

    }

}