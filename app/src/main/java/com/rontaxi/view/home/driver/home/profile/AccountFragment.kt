package com.rontaxi.view.home.driver.home.profile

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.socket.SocketManager
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.loadProfileImageFromURL
import com.rontaxi.util.showAlertWithCancel
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.driver.home.payment.BankDetailFragment
import com.rontaxi.view.home.driver.home.profile.basicinfo.BasicInformationFragment
import com.rontaxi.view.home.driver.home.profile.updatephone.ChangePhoneFragment
import com.rontaxi.view.home.rider.changepassword.ChangePasswordFragment
import com.rontaxi.view.home.rider.help.HelpFragment
import com.rontaxi.view.home.rider.ridehistory.RideHistoryFragment
import com.rontaxi.view.home.rider.ridehistory.RidesContainerFragment
import com.rontaxi.view.initial.driver.upload.UploadDocumentFragment
import com.rontaxi.view.splash.SplashActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class AccountFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var socketManager: SocketManager

    @Inject
    lateinit var accountViewModel: AccountViewModel

    override fun getLayoutRes() = R.layout.fragment_account

    override fun showTitleBar() = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setInformation()

        if (isLoaded) {
            return
        }
        clickListners()
        addObserver()
    }

    fun setInformation() {
        setToolBar()

        val user = accountViewModel.getUserProfile()
        tvDriverName.text = "${user.firstName} ${user.lastName}"

        if (user.driverImage.isNotEmpty()) {
            ivProfilePic.loadProfileImageFromURL(context!!, user.driverImage)
        }
    }

    private fun clickListners() {
        clBasicInfo.setOnClickListener(this)
        clHelp.setOnClickListener(this)
        clLogout.setOnClickListener(this)
        clDoccuments.setOnClickListener(this)
        clChangePass.setOnClickListener(this)
        clChangePhone.setOnClickListener(this)
        clAddBankAcc.setOnClickListener(this)

        clTripHistory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v) {
            clBasicInfo -> {
                (activity as HomeDriverActivity).replaceFragment(BasicInformationFragment(), true)
            }

            clHelp -> {


                val frag = HelpFragment()
                frag.isRaiseIssue = false
                (activity as HomeDriverActivity).replaceFragment(frag, true)
                //  (activity as HomeDriverActivity).replaceFragment(HelpFragment(),true)
            }

            clDoccuments -> {
                (activity as HomeDriverActivity).replaceFragment(UploadDocumentFragment(), true)
            }

            clChangePass -> {
                (activity as HomeDriverActivity).replaceFragment(ChangePasswordFragment(), true)
            }

            clChangePhone -> {
                (activity as HomeDriverActivity).replaceFragment(ChangePhoneFragment(), true)

            }

            clAddBankAcc -> {
                (activity as HomeDriverActivity).replaceFragment(BankDetailFragment(), true)

            }


            clTripHistory -> {
                (activity as HomeDriverActivity).replaceFragment(RidesContainerFragment(), true)
            }

            clLogout -> {

                showAlertWithCancel(
                    activity!!,
                    resources.getString(R.string.are_you_sure_you_want_to_logout),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    {

                        ProgressDialog.showProgressBar(activity!!, "", false)
                        accountViewModel.logout()
                    },
                    {})


            }
        }
    }

    private fun setToolBar() {
        toolbarTitle.text = resources.getString(R.string.profile)

    }

    private fun addObserver() {
        accountViewModel.logoutLiveData.observe(this, Observer {
            it!!.apply {
                ProgressDialog.hideProgressBar()
                if (it.isSuccessful) {
                    Toasty.success(activity!!, it.body()!!.message).show()
                    accountViewModel.clearData()
                    accountViewModel.disconnectSockets()


                    val intent = Intent(activity!!, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })
    }
}