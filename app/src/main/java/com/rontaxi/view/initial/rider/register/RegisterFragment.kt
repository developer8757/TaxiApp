package com.rontaxi.view.initial.rider.register

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.rontaxi.base.BaseFragment
import com.google.gson.Gson
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import com.rontaxi.view.initial.rider.InitialRiderActivity
import com.rontaxi.view.initial.rider.otp.OtpFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.cbTerms
import kotlinx.android.synthetic.main.fragment_register.countryCodePicker
import kotlinx.android.synthetic.main.fragment_register.etEmail
import kotlinx.android.synthetic.main.fragment_register.etFirstName
import kotlinx.android.synthetic.main.fragment_register.etLastName
import kotlinx.android.synthetic.main.fragment_register.etPassword
import kotlinx.android.synthetic.main.fragment_register.etPhone

import javax.inject.Inject

class RegisterFragment: BaseFragment() {

    @Inject
    lateinit var registerViewModel: RegisterViewModel

    override fun getLayoutRes()=R.layout.fragment_register

    override fun showTitleBar()=false

    lateinit var phone: Phone


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(isLoaded){
            return
        }
        setClicks()
        setObservers()

    }

    private fun setObservers(){

        registerViewModel.registerLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()

            if(it!!.isSuccessful){

                Toasty.success(context!!,it.body()!!.message).show()

                registerViewModel.saveToken()

                var fragment=OtpFragment()
                    fragment.navigation= OtpFragment.NAVIGATION.REGISTERATION
                    fragment.phone=phone

                (activity as InitialRiderActivity).replaceFragment(fragment,false)

            }else{

                Toasty.error(context!!,getErrorMessage(it.errorBody()!!).message).show()

            }

        })

    }

    private fun setClicks(){
        btnRegister.setOnClickListener {

            hideKeyBoard(context!!, view!!)

            if(InternetCheck.isConnectedToInternet(context!!)
                && Validations.isEmpty(context!!,etFirstName)
                && Validations.isEmpty(context!!,etLastName)
                && Validations.isValidEmail(context!!,etEmail)
                && Validations.isValidPhoneNumber(context!!, etPhone)
                && Validations.isEmpty(context!!,etPassword)
                && Validations.isValidPassword(context!!,etPassword)
                && Validations.isAlphaNumeric(context!!,etPassword)
                && Validations.isTermsAccepted(cbTerms,context!!)
            ){


                phone=Phone()
                phone.number=etPhone.text.toString().trim()
                phone.code="+${countryCodePicker.selectedCountryCode}"

                ProgressDialog.showProgressBar(context!!,getString(R.string.registering_rider),false)

                var requestModel= RegisterRequestModel()

                requestModel.firstName= getRequestBody(etFirstName.text.toString().trim())
                requestModel.lastName= getRequestBody(etLastName.text.toString().trim())
                requestModel.email= getRequestBody(etEmail.text.toString().trim())
                requestModel.phone= getRequestBody(Gson().toJson(phone))
                requestModel.password=getRequestBody(etPassword.text.toString().trim())

                requestModel.userActiveRole= getRequestBody(BuildConfig.ROLE.toString())


                registerViewModel.register(requestModel)
            }



        }

    }
}