package com.rontaxi.view.home.driver

import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import com.andrognito.flashbar.Flashbar
import com.rontaxi.base.BaseActivity
import com.rontaxi.R
import com.rontaxi.cache.getUser
import com.rontaxi.location.LocationUpdateService
import com.rontaxi.util.InternetCheck
import com.rontaxi.view.home.driver.home.HomeDriverFragment
import com.rontaxi.view.home.driver.home.payment.BankDetailFragment
import com.rontaxi.view.home.rider.godview.GodViewFragment
import com.rontaxi.view.home.rider.profile.RiderProfileFragment
import com.util.FragmentUtil

class HomeDriverActivity : BaseActivity(), LifecycleObserver {

    val isConnected = MutableLiveData<Boolean>().apply {
        this.postValue(true)
    }

    val homeDriverFragment = HomeDriverFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_driver)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)


        isConnected.postValue(InternetCheck.isConnectedToInternet_(this@HomeDriverActivity))


        replaceFragment(homeDriverFragment, false)

        registerNetworkBroadCast()

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

    override fun onBackPressed() {


        val currentFrag=supportFragmentManager.findFragmentById(R.id.container)

        when(currentFrag){

            is BankDetailFragment ->{

                currentFrag.onBackPressed()
                return

            }



        }
        super.onBackPressed()
    }

    fun startService() {
        if(getUser(this)!=null) {
            val serviceIntent = Intent(this, LocationUpdateService::class.java)




            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }

    fun stopService() {
       val serviceIntent = Intent(this, LocationUpdateService::class.java)
        this.stopService(serviceIntent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onConnect(){
        Log.i("APp_lifecycle", "resume")
        stopService()

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onDisconnect(){
        Log.i("APp_lifecycle", "pause")
        startService()
    }





}