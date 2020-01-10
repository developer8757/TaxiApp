package com.rontaxi.view.initial.rider.resetpassword

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.util.hideKeyBoard
import com.rontaxi.util.showAlert
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_driver_reset_password.*
import javax.inject.Inject

class ResetPasswordFragment : BaseFragment() {

    @Inject
    lateinit var resetPasswordViewModel: ResetPasswordViewModel

    override fun getLayoutRes() = R.layout.fragment_reset_password

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isLoaded) {
            return
        }

        setClicks()
        setObserver()
        val list = listOf("red", "green")
        val list2 = mutableListOf("red", "green")
        list - "blue"
    }


    private fun setClicks() {

        btnSubmit.setOnClickListener {

            hideKeyBoard(context!!, view!!)

            if (Validations.isEmpty(context!!, etPassword)
                && Validations.isValidPassword(context!!, etPassword)
                && Validations.isAlphaNumeric(context!!, etPassword)
                && Validations.isEmpty(context!!, etConfirmPassword)
                && Validations.isValidPassword(context!!, etConfirmPassword)
                && Validations.confirmPassword(context!!, etPassword, etConfirmPassword)

            ) {
                ProgressDialog.showProgressBar(context!!, getString(R.string.changing_password), false)

                resetPasswordViewModel.resetPassword(etPassword.text.toString().trim())

            }
        }
    }

    private fun setObserver() {
        resetPasswordViewModel.resetPasswordLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                Toasty.success(context!!, it.body()!!.message).show()

                (activity)!!.supportFragmentManager.popBackStack()


            } else {

                showAlert(context!!, getErrorMessage(it.errorBody()!!).message, getString(R.string.ok), {})
            }
        })

    }
}
