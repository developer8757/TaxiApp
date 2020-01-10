package com.rontaxi.view.initial.rider.otp

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.rider.InitialRiderActivity
import com.rontaxi.view.initial.rider.resetpassword.ResetPasswordFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.btnSubmit
import javax.inject.Inject
import android.view.ViewTreeObserver
import com.rontaxi.util.*


class OtpFragment: BaseFragment() {

    enum class NAVIGATION{

        FORGOT_PASSWORD,
        CHANGE_PHONE,
        REGISTERATION,
        UNVERIFIED,

    }

    var loginToken: String?=""
    lateinit var phone: Phone

    lateinit var navigation: NAVIGATION

    @Inject

    lateinit var otpViewModel: OtpViewModel

    override fun getLayoutRes()=R.layout.fragment_otp

    override fun showTitleBar()=false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(isLoaded){
            return
        }


        setObserver()
        setClicks()
        view?.viewTreeObserver?.addOnGlobalLayoutListener(keyBoardObserver)

        setInformation()
    }


    private fun setInformation(){
        tvPhoneNumber.text="${phone.code} ${phone.number}"
    }

    override fun onPause() {

        view?.viewTreeObserver?.removeOnGlobalLayoutListener(keyBoardObserver)
        super.onPause()


    }




    private fun setObserver(){
        otpViewModel.otpVerificationLiveData.observe(this, Observer {


            ProgressDialog.hideProgressBar()
            if(it!!.isSuccessful){

                Toasty.success(context!!, it.body()!!.message).show()

                otpViewModel.saveToken()
                otpViewModel.saveUser()

                when(navigation){

                    NAVIGATION.FORGOT_PASSWORD->{
                        (activity as InitialRiderActivity).replaceFragment(ResetPasswordFragment(),false)
                    }

                    NAVIGATION.CHANGE_PHONE->{

                        fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                    NAVIGATION.REGISTERATION->{

                        otpViewModel.connectSockets()

                        activity!!.startActivity(Intent(activity, HomeRiderActivity::class.java))
                        activity!!.finish()

                    }

                    NAVIGATION.UNVERIFIED->{

                        popBackStack()
                    }

                }

            }else{

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})
            }

        })

        otpViewModel.resendOtpLiveData.observe(this,Observer{
            ProgressDialog.hideProgressBar()
            if(it!!.isSuccessful) {

                Toasty.success(context!!, it.body()!!.message).show()
            }else{

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})
            }

        })
    }


    private fun setClicks(){
        btnSubmit.setOnClickListener {


            hideKeyBoard(context!!,view!!)




            if(otpView.text.toString().trim().length<4){
                Toasty.error(context!!,"Please enter valid OTP").show()
            }else{
                ProgressDialog.showProgressBar(context!!,getString(R.string.verifying_user),false)


                when(navigation) {
                    NAVIGATION.CHANGE_PHONE->{

                        otpViewModel.verifyOtp(otpView.text.toString().trim(),loginToken!!)
                    }

                    else ->{
                        otpViewModel.verifyOtp(otpView.text.toString().trim(), null)
                    }
                }


            }


        }

        btnResend.setOnClickListener {
            ProgressDialog.showProgressBar(context!!,getString(R.string.requesting_otp),false)

            otpViewModel.resendOtp()
        }
    }



    var keyBoardObserver=object: ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {

            val heightDiff = view!!.getRootView().getHeight() - view!!.height
            if (heightDiff > dpToPx(context!!, 200f)) { // if more than 200 dp, it's probably a keyboard...
                // ... do something here
                otpView.setTextColor(resources.getColor(R.color.md_white_1000))
                otpView.setLineColor(resources.getColor(R.color.md_white_1000))
                otpView.cursorColor=resources.getColor(R.color.md_white_1000)
                tvFourDigitCode.setTextColor(resources.getColor(R.color.md_white_1000))
            }else{
                tvFourDigitCode.setTextColor(resources.getColor(R.color.md_black_1000))
                otpView.setTextColor(resources.getColor(R.color.md_black_1000))
                otpView.setLineColor(resources.getColor(R.color.md_black_1000))
                otpView.cursorColor=resources.getColor(R.color.md_black_1000)
            }
        }
    }

}