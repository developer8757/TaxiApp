package com.rontaxi.view.home.rider.changepassword

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.cache.clearAllData
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.util.hideKeyBoard
import com.rontaxi.util.showAlert
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.rider.InitialRiderActivity
import com.rontaxi.view.splash.SplashActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_changepassword.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class ChangePasswordFragment : BaseFragment() {

    @Inject
    lateinit var changePasswordViewModel: ChangePasswordViewModel


    override fun getLayoutRes() = R.layout.fragment_changepassword
    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isLoaded) {
            return
        }

        setClicks()
        setObserver()
        setToolBar()
    }

    private fun setToolBar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.change_password)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llEnd.removeAllViews()
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
    }


    private fun setClicks() {
        btnChangeSubmit.setOnClickListener {
            if (Validations.isEmpty(context!!, oldChangePassword)
                && Validations.isEmpty(context!!, etChangeNewPassword)
                && Validations.isAlphaNumeric(context!!,etChangeNewPassword)
                && Validations.isEmpty(context!!, etChangeConfirmPassword)
                && Validations.confirmPassword(context!!, etChangeNewPassword, etChangeConfirmPassword)) {

                val oldPassword = oldChangePassword.text.toString().trim()
                val newPassword = etChangeNewPassword.text.toString().trim()
                hideKeyBoard(context!!, view!!)
                ProgressDialog.showProgressBar(context!!, getString(R.string.changing_password), false)
                changePasswordViewModel.changePassword(oldPassword, newPassword)
            }
        }
    }

    private fun setObserver() {
        /* changePasswordViewModel.logoutLiveData.observe(this, Observer {
             it!!.apply {
                 ProgressDialog.hideProgressBar()
                 if (it.isSuccessful) {
                     clearAllData(activity!!)
                     val intent = Intent(context!!, InitialRiderActivity::class.java)
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                     startActivity(intent)
                 } else
                     tackleError(activity!!, getErrorMessage(it.errorBody()!!))
             }
         })
 */


        changePasswordViewModel.changePasswordLiveData.observe(this, Observer {
            it?.apply {
                ProgressDialog.hideProgressBar()
                if (isSuccessful) {
                    Toasty.success(context!!, it.body()!!.message).show()
                    clearAllData(activity!!)
                    val intent = Intent(context!!, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    //changePasswordViewModel.logout()
                } else
                    showAlert(context!!, getErrorMessage(it.errorBody()!!).message, getString(R.string.ok), {})
            }
        })
    }
}