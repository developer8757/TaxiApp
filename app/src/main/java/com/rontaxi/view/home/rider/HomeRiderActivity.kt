package com.rontaxi.view.home.rider

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.rontaxi.R
import com.rontaxi.broadcasts.NetworkBroadCast
import com.rontaxi.cache.getUser
import com.rontaxi.view.home.rider.godview.GodViewFragment
import com.rontaxi.view.home.rider.profile.RiderProfileFragment
import com.util.FragmentUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_drawer_layout.view.*
import android.content.Context
import android.content.Intent
import android.net.*
import android.util.Log
import com.andrognito.flashbar.Flashbar
import com.rontaxi.base.BaseActivity
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.changepassword.ChangePasswordFragment
import com.rontaxi.view.home.rider.help.HelpFragment
import com.rontaxi.view.home.rider.payment.PaymentFragment
import com.rontaxi.view.home.rider.ridehistory.RideHistoryFragment
import com.rontaxi.view.home.rider.ridehistory.RidesContainerFragment
import com.rontaxi.view.splash.SplashActivity
import es.dmoral.toasty.Toasty
import javax.inject.Inject


class HomeRiderActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var homeRiderViewModel: HomeRiderViewModel

    val isConnected = MutableLiveData<Boolean>().apply {
        this.postValue(true)
    }

    val godViewFragment: GodViewFragment by lazy {
        GodViewFragment()
    }

    val networkBroadCast: NetworkBroadCast by lazy {

        NetworkBroadCast()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isConnected.postValue(InternetCheck.isConnectedToInternet_(this@HomeRiderActivity))

        replaceFragment(godViewFragment, false)

        nav_view.setNavigationItemSelectedListener(this)

        registerNetworkBroadCast()

        addObserver()
    }

    override fun onResume() {
        super.onResume()
        setUserInformation()
    }

    fun replaceFragment(fragment: Fragment, keepInStack: Boolean) {

        FragmentUtil.replaceFragment(
            this,
            fragment,
            R.id.container,
            keepInStack,
            FragmentUtil.TRANSITION_NONE
        )
    }


    fun openCloseDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            drawer_layout.openDrawer(GravityCompat.START)

    }

    fun setUserInformation() {

        var user = getUser(this)

        nav_view.getHeaderView(0).tvProfileName.text = "${user?.firstName} ${user?.lastName}"

        if (!user!!.profileImage.isNullOrEmpty()) {
            nav_view.getHeaderView(0).ivProfile.loadProfileImageFromURL(this, user!!.profileImage)
        } else {

        }

        nav_view.getHeaderView(0).ratingRiderDetail.rating = user.rating.toFloat()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {


            R.id.nav_rider_profile -> {
                replaceFragment(RiderProfileFragment(), true)

            }

            R.id.nav_rider_trips -> {
                //  replaceFragment(RideHistoryFragment(), true)
                replaceFragment(RidesContainerFragment(), true)
            }
            R.id.nav_rider_payments -> {
                replaceFragment(PaymentFragment(), true)
            }
            R.id.nav_rider_change_password -> {
                replaceFragment(ChangePasswordFragment(), true)
            }
            R.id.nav_rider_logout -> {

                showAlertWithCancel(
                    this,
                    resources.getString(R.string.are_you_sure_you_want_to_logout),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    {

                        ProgressDialog.showProgressBar(this, "", false)
                        homeRiderViewModel.logout()
                    },
                    {})

            }
            R.id.nav_rider_information -> {

                val frag = HelpFragment()
                frag.isRaiseIssue = false
                replaceFragment(frag, true)
            }
            else -> {
                replaceFragment(RiderProfileFragment(), true)
            }

        }

        openCloseDrawer()

        return true
    }


    override fun onBackPressed() {


        val currentFrag = supportFragmentManager.findFragmentById(R.id.container)

        when (currentFrag) {

            is GodViewFragment -> {

                currentFrag.onBackPressed()
                return

            }

            is RiderProfileFragment -> {

                currentFrag.onBackPressed()
                return

            }

        }
        super.onBackPressed()
    }


    val connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    val flashBar: Flashbar by lazy {

        Flashbar.Builder(this)
            .gravity(Flashbar.Gravity.TOP)
            .message("\n\nYou are offline")
            .backgroundColor(resources.getColor(R.color.md_red_A700))
            .messageColor(resources.getColor(R.color.md_white_1000))
            .messageSizeInSp(12f)
            // .duration(Flashbar.DURATION_INDEFINITE)
            .build()

    }

    val TAG = "NETWORK_BROADCAST"

    fun registerNetworkBroadCast() {

        val request = NetworkRequest.Builder()
        request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        connectivityManager.requestNetwork(request.build(),
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network?) {
                    super.onAvailable(network)

                    Log.i(TAG, "AVAILABLE")
                    isConnected.postValue(true)


                }


                override fun onLosing(network: Network?, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)

                    Log.i(TAG, "LOSING")
                }

                override fun onLost(network: Network?) {
                    super.onLost(network)


                    isConnected.postValue(false)
                }

            })

        isConnected.observe(this, Observer {

            it?.let {
                if (!it) {
                    flashBar.show()
                } else {
                    flashBar.dismiss()
                }

            }

        })

    }


    private fun addObserver() {
        homeRiderViewModel.logoutLiveData.observe(this, Observer {
            it!!.apply {
                ProgressDialog.hideProgressBar()
                if (it.isSuccessful) {
                    Toasty.success(this@HomeRiderActivity, it.body()!!.message).show()
                    homeRiderViewModel.clearData()
                    homeRiderViewModel.disconnectSockets()


                    val intent = Intent(this@HomeRiderActivity, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else
                    tackleError(this@HomeRiderActivity, getErrorMessage(it.errorBody()!!))
            }
        })
    }


}