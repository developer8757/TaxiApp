package com.rontaxi.view.initial.rider

import android.os.Bundle
import android.support.v4.app.Fragment
import com.rontaxi.base.BaseActivity
import com.rontaxi.R
import com.rontaxi.view.initial.rider.login.LoginFragment
import com.util.FragmentUtil

class InitialRiderActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        replaceFragment(LoginFragment(),false)
    }
    fun replaceFragment(fragment: Fragment, keepInStack: Boolean){
        FragmentUtil.replaceFragment(this,fragment,R.id.container,keepInStack, FragmentUtil.TRANSITION_NONE)
    }
}