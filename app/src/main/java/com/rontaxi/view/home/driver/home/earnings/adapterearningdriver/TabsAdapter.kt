package com.rontaxi.view.home.driver.home.earnings.adapterearningdriver

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.rontaxi.view.home.driver.home.earnings.DriverEarningTodayFragment

class TabsAdapter( fm:FragmentManager) :
    FragmentPagerAdapter(fm) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
   override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }
    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }



}