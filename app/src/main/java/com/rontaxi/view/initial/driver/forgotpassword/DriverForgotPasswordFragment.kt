package com.rontaxi.view.initial.driver.forgotpassword

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.otp.DriverOtpFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_driver_forgot_password.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class DriverForgotPasswordFragment: BaseFragment() {


    lateinit var phone:Phone



    @Inject
    lateinit var forgotPasswordViewModel: DriverForgotPasswordViewModel


    override fun getLayoutRes(): Int {

        return R.layout.fragment_driver_forgot_password
    }

    override fun showTitleBar()=false







    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if(isLoaded){
            return
        }

        setClicks()
        setObservers()

        setToolbar()

    }





    private fun setClicks(){

        btnSend.setOnClickListener {



            if(InternetCheck.isConnectedToInternet(context!!)
                &&Validations.isValidPhoneNumber(context!!, etPhone)){

                hideKeyBoard(context!!, view!!)

                ProgressDialog.showProgressBar(context!!, getString(R.string.wait), false)


                phone= Phone()
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


                    var fragment=DriverOtpFragment()
                        fragment.phone=phone
                        fragment.navigation=DriverOtpFragment.NAVIGATION.FORGOT_PASSWORD


                // save temp access token in preferences
                forgotPasswordViewModel.saveToken()

                (activity as InitialDriverActivity).replaceFragment(fragment,false)



            }else{

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})

            }
        })

    }

    private fun setToolbar() {

        val textTitle = TextView(context)

        textTitle.text = resources.getString(R.string.forgot_password_)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.sp_20));


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
}