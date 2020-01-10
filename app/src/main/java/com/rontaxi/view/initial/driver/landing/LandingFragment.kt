package com.rontaxi.view.initial.driver.landing

import android.os.Bundle
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.login.DriverLoginFragment
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import kotlinx.android.synthetic.main.fragment_driver_landing.*

class LandingFragment: BaseFragment() {

    override fun getLayoutRes()=R.layout.fragment_driver_landing

    override fun showTitleBar()=false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClicks()

    }


    private fun setClicks(){

        btnLogin.setOnClickListener {

            (activity as InitialDriverActivity).replaceFragment(DriverLoginFragment(),true)


        }

        btnRegister.setOnClickListener {
            (activity as InitialDriverActivity).replaceFragment(DriverRegisterFragment(),true)


        }

    }
}