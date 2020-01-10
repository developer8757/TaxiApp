package com.rontaxi.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment: Fragment() {
    var baseView: View?=null
    var isLoaded=false

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    abstract fun showTitleBar():Boolean

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        isLoaded=true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as BaseActivity).showActionBar(showTitleBar())
        if(baseView==null)
            baseView=inflater.inflate(getLayoutRes(),container,false)
        return baseView
    }

    fun setTitle(title: String){
        (activity as BaseActivity).setTitle(title)
    }

    fun popBackStack(){
        (activity as BaseActivity).popBackStack()
    }
}