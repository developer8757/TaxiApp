package com.rontaxi.view.initial.rider.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.socket.SocketManager
import com.rontaxi.util.*
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.erroraction.showErrorMessage
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.driver.login.LoginRequestModel
import com.rontaxi.view.initial.rider.InitialRiderActivity
import com.rontaxi.view.initial.rider.forgotpassword.ForgotPasswordFragment
import com.rontaxi.view.initial.rider.register.RegisterFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.btnLogin
import kotlinx.android.synthetic.main.fragment_login.etPassword
import kotlinx.android.synthetic.main.fragment_login.etPhone
import javax.inject.Inject

class LoginFragment: BaseFragment() {



    @Inject
    lateinit var viewModel: LoginViewModel



    override fun getLayoutRes()=R.layout.fragment_login
    override fun showTitleBar()=false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       view?.viewTreeObserver?.addOnGlobalLayoutListener(keyBoardObserver)

        if(isLoaded){
            return
        }
       setClicks()
        setObserver()

    }

    override fun onPause() {

        view?.viewTreeObserver?.removeOnGlobalLayoutListener(keyBoardObserver)
        super.onPause()


    }



    private fun setObserver(){

        viewModel.loginLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()

            if(it!!.isSuccessful ){
                if(isResponseValid(activity!!,it.body()!!)){


                    viewModel.saveUser()
                    viewModel.saveToken()
                    viewModel.connectSockets()
                    Toasty.success(context!!,it.body()!!.message).show()



                    startActivity(Intent(activity, HomeRiderActivity::class.java))
                    (activity!!).finish()
                }

            }else{

              //  tackleErrorNavigation(activity!!,it.errorBody()!!)

               showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})
            }

        })

    }

    private fun setClicks(){

        btnSignUp.setOnClickListener {


            (activity as InitialRiderActivity).replaceFragment(RegisterFragment(),true)



        }

        btnLogin.setOnClickListener {

            if(InternetCheck.isConnectedToInternet(context!!)
                && Validations.isValidPhoneNumber(context!!,etPhone)
                && Validations.isEmpty(context!!,etPassword)){



                var loginRequestModel= LoginRequestModel()
                loginRequestModel.phone.number=etPhone.text.toString().trim()
                loginRequestModel.phone.code="+${countryCodePicker.selectedCountryCode}"

                loginRequestModel.password=etPassword.text.toString().trim()


                hideKeyBoard(context!!,view!!)



                ProgressDialog.showProgressBar(context!!,getString(R.string.loging_in),false)
                viewModel.login(loginRequestModel)
            }

        }

        tvForgotPassword.setOnClickListener {

            (activity as InitialRiderActivity).replaceFragment(ForgotPasswordFragment(),true)


        }
    }

    var keyBoardObserver=object: ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {

            val heightDiff = view!!.getRootView().getHeight() - view!!.height
            if (heightDiff > dpToPx(context!!, 200f)) { // if more than 200 dp, it's probably a keyboard...
                // ... do something here
                ivDiv.visibility= View.GONE
                btnSignUp.visibility= View.GONE

            }else{

                ivDiv.visibility= View.VISIBLE
                btnSignUp.visibility= View.VISIBLE
            }
        }
    }
}