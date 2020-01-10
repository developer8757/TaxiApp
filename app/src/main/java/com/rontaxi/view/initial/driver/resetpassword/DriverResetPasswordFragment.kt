package com.rontaxi.view.initial.driver.resetpassword

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.util.hideKeyBoard
import com.rontaxi.util.showAlert
import com.rontaxi.view.initial.driver.InitialDriverActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_driver_reset_password.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class DriverResetPasswordFragment : BaseFragment(){



    @Inject
    lateinit var driverResetPasswordViewModel: DriverResetPasswordViewModel

    override fun getLayoutRes()=R.layout.fragment_driver_reset_password

    override fun showTitleBar()=false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(isLoaded){
            return
        }

        setToolbar()

        setClicks()

        setObserver()
    }


    private fun setToolbar() {

        val textTitle = TextView(context)

        textTitle.text = getString(R.string.reset_password)
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

    private fun setClicks(){

        btnSubmit.setOnClickListener {

            hideKeyBoard(context!!,view!!)

            if(Validations.isEmpty(context!!,etPassword)
                && Validations.isValidPassword(context!!, etPassword)
                && Validations.isAlphaNumeric(context!!, etPassword)
                && Validations.isEmpty(context!!,etConfirmPassword)
                && Validations.confirmPassword(context!!,etPassword,etConfirmPassword)

            ){
                ProgressDialog.showProgressBar(context!!,getString(R.string.changing_password),false)

                driverResetPasswordViewModel.resetPassword(etPassword.text.toString().trim())

            }
        }
    }

    private fun setObserver(){
        driverResetPasswordViewModel.resetPasswordLiveData.observe(this, Observer {


            ProgressDialog.hideProgressBar()
            if(it!!.isSuccessful){

                Toasty.success(context!!,it.body()!!.message).show()

                (activity)!!.supportFragmentManager.popBackStack()


            }else{

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})
            }
        })

    }

}