package com.rontaxi.view.home.driver.home.payment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getUser
import com.rontaxi.cache.saveUser
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.showAlert
import com.rontaxi.util.showAlertWithCancel
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.initial.driver.InitialDriverActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_bank_detail.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class BankDetailFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var bankDetailViewModel: BankDetailViewModel

    override fun getLayoutRes() = R.layout.fragment_bank_detail

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isLoaded) {

            getBankDetails()
            return
        }
        //setDetails()
        setListeners()
        addObserver()
        setToolBar()

        getBankDetails()
    }


    private fun getBankDetails(){

        ProgressDialog.showProgressBar(context!!,"",false)
        bankDetailViewModel.getBankAccount()
    }

    override fun onResume() {
        super.onResume()
       // setDetails()

    }



    private fun addObserver() {
        bankDetailViewModel.deleteCardLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            it?.apply {

                if (isSuccessful) {

                    bankDetailViewModel.saveUser()

                    Toasty.success(context!!, getString(R.string.payment_deleted)).show()
                    clAddBankAccount.visibility = View.VISIBLE
                    cvShowBankDetails.visibility = View.GONE
                } else {
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))

                }
            }
        })


        bankDetailViewModel.paymentListLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()

            it?.apply {
                if (isSuccessful) {


                    if (this.body()!!.data!!.payment.isNullOrEmpty()) {
                        clAddBankAccount.visibility = View.VISIBLE
                        cvShowBankDetails.visibility = View.GONE

                    } else {

                        clAddBankAccount.visibility = View.GONE
                        cvShowBankDetails.visibility = View.VISIBLE
                        tvBankName.text=this.body()!!.data!!.payment!![0].bank_name

                        tvBankAccountNo.text = "**********${this.body()!!.data!!.payment!![0].last4}"

                        if (this.body()!!.data!!.isAccountAdded == 1) {
                            tvAccountStatus.text = getString(R.string.verification_pending)


                        } else if (this.body()!!.data!!.isAccountAdded == 2) {
                            tvAccountStatus.text = getString(R.string.status_verified)
                        }

                    }

                    var user= getUser(context!!)

                    user!!.isAccountAdded=this.body()!!.data!!.isAccountAdded
                    user!!.payment=this.body()!!.data!!.payment

                    saveUser(context!!,user)

                } else {
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))

                }
            }
        })
    }

    private fun setListeners() {
        btnAddBankAccount.setOnClickListener(this)
        btnRemoveAccount.setOnClickListener(this)

    }

    private fun setDetails() {
        var user = getUser(context!!)

        user?.apply {

            if (this.payment.isNullOrEmpty()) {
                clAddBankAccount.visibility = View.VISIBLE
                cvShowBankDetails.visibility = View.GONE

            } else {

                clAddBankAccount.visibility = View.GONE
                cvShowBankDetails.visibility = View.VISIBLE
                tvBankName.text=this.payment!![0].bank_name
                tvBankAccountNo.text = "**********${this.payment!![0].last4}"

                if (this.isAccountAdded == 1) {
                    tvAccountStatus.text = getString(R.string.verification_pending)


                } else if (this.isAccountAdded == 2) {
                    tvAccountStatus.text = getString(R.string.status_verified)
                }

            }
        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnAddBankAccount -> {
                if (activity is HomeDriverActivity) {

                    (activity as HomeDriverActivity).replaceFragment(AddBankFragment(), true)
                } else {
                    (activity as InitialDriverActivity).replaceFragment(AddBankFragment(), true)

                }
            }

            R.id.btnRemoveAccount -> {
                deleteAccount()
            }
        }

    }

    private fun deleteAccount() {


        showAlertWithCancel(context!!, "Are you sure want to delete the bank account detail?",
            getString(R.string.ok),
            getString(R.string.cancel),{
                ProgressDialog.showProgressBar(context!!, "", false)
                var user = getUser(context!!)

                user?.apply {

                    bankDetailViewModel.deleteAccount(this.payment!![0].id!!)

                }

            },{})



    }

    private fun setToolBar() {
        toolbarTitle.text = resources.getString(R.string.add_bank_account)
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            activity!!.onBackPressed()

        }
    }


    fun onBackPressed(){

        var user = getUser(context!!)

        user?.apply {


            if(this.payment.isNullOrEmpty()){
                showAlert(context!!, getString(R.string.add_bank_account_alert),
                    getString(R.string.ok),
                   {})

                return
            }

           if(user.isAccountAdded==1){

               showAlert(context!!, getString(R.string.verification_pending_alert),
                   getString(R.string.ok),{


                       activity!!.finish()
                       val intent = Intent(context, InitialDriverActivity::class.java)
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                       startActivity(intent)

                   })

               return

           }
        }

        (activity)!!.supportFragmentManager.popBackStackImmediate()


    }
}