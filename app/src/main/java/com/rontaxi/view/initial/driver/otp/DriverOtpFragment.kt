package com.rontaxi.view.initial.driver.otp

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.util.hideKeyBoard
import com.rontaxi.util.showAlert
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.resetpassword.DriverResetPasswordFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_driver_otp.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class DriverOtpFragment: BaseFragment() {


    enum class NAVIGATION{
        FORGOT_PASSWORD,
        CHANGE_PHONE,
        REGISTERATION,
        UNVERIFIED
    }


    var loginToken: String?=""
    lateinit var phone: Phone

    lateinit var navigation:NAVIGATION

    @Inject
    lateinit var driverOtpViewModel: DriverOtpViewModel


    override fun getLayoutRes()=R.layout.fragment_driver_otp

    override fun showTitleBar()=false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(isLoaded){
            return
        }

        setToolbar()
        setInformation()
        setClicks()
        setTextWatcher()
        setObserver()


    }


    private fun setObserver(){
        driverOtpViewModel.otpVerificationLiveData.observe(this, Observer {


            ProgressDialog.hideProgressBar()
            if(it!!.isSuccessful){

                Toasty.success(context!!, it.body()!!.message).show()

                driverOtpViewModel.saveToken()
                driverOtpViewModel.saveUser()

                when(navigation){

                    NAVIGATION.FORGOT_PASSWORD->{
                        (activity as InitialDriverActivity).replaceFragment(DriverResetPasswordFragment(),false)
                    }

                    NAVIGATION.CHANGE_PHONE->{

                        fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                    NAVIGATION.REGISTERATION->{

                        activity!!.startActivity(Intent(context!!, InitialDriverActivity::class.java))
                        activity!!.finish()

                    }

                }

            }else{

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})
            }

        })

        driverOtpViewModel.resendOtpLiveData.observe(this,Observer{
            ProgressDialog.hideProgressBar()
            if(it!!.isSuccessful) {

                Toasty.success(context!!, it.body()!!.message).show()
            }else{

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})
            }

        })
    }

    private fun setTextWatcher(){

        etOtp.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().trim().length==4){
                    btnSubmit.isEnabled=true
                    return
                }
                btnSubmit.isEnabled=false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setClicks(){
        btnSubmit.setOnClickListener {

            hideKeyBoard(context!!,view!!)

            if(Validations.isEmpty(context!!,etOtp)){

                ProgressDialog.showProgressBar(context!!,getString(R.string.verifying_user),false)

                when(navigation) {
                    NAVIGATION.CHANGE_PHONE->{

                        driverOtpViewModel.verifyOtp(etOtp.text.toString().trim(),loginToken!!)
                    }

                    else ->{
                        driverOtpViewModel.verifyOtp(etOtp.text.toString().trim(), null)
                    }
                }

            }
        }

        tvResend.setOnClickListener {
            ProgressDialog.showProgressBar(context!!,getString(R.string.requesting_otp),false)

            driverOtpViewModel.resendOtp()
        }
    }

    private fun setInformation(){

        tvInformation.text="${getString(R.string.otp_information)}${phone.code} ${phone.number}"
    }


    private fun setToolbar() {

        val textTitle = TextView(context)

        textTitle.text = getString(R.string.mobile_verify)
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
            activity!!.onBackPressed()
        }

    }

}