package com.rontaxi.view.home.driver.home.earnings

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.view.home.driver.home.earnings.adapterearningdriver.TabsAdapter
import kotlinx.android.synthetic.main.fragment_earnings.*
import javax.inject.Inject
import android.util.Log
import com.rontaxi.model.drivingearning.DrivingEarningData
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.erroraction.tackleError
import kotlinx.android.synthetic.main.fragment_driver_earning_today.*


class EarningsFragment : BaseFragment() {


    @Inject
    lateinit var driverEarningViewModel: DriverEarningViewModel
    val todayFragment = DriverEarningTodayFragment()
    val totalFragment = DriverEarningTodayFragment()
    var selectedTabPosition = 0

    var isDataLoading: Boolean = false

    override fun getLayoutRes() = com.rontaxi.R.layout.fragment_earnings
    override fun showTitleBar() = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        callEarning()

        if (isLoaded) {
            return
        }

        todayFragment.listHeading = getString(R.string.driver_today_trip)
        totalFragment.listHeading = getString(R.string.driver_weekly_trip)

        addObserver()
        addTab()
        setAdapter()
        onClick()

    }

    fun callEarning() {

        isDataLoading = true
        if (todayFragment.offset < 1 || totalFragment.offset < 1)
            ProgressDialog.showProgressBar(context!!, "", false)
        if (selectedTabPosition == 0) {
            driverEarningViewModel.driverEarning(1, todayFragment.offset)
        } else if (selectedTabPosition == 1) {
            driverEarningViewModel.driverEarning(2, totalFragment.offset)
        }
    }

    private fun addObserver() {
        driverEarningViewModel.driverEarningLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()

            isDataLoading = false

            if (it!!.isSuccessful) {
                drivingEarningData(it.body()?.data!!)
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })


    }


    private fun addTab() {
        tabLayout?.setupWithViewPager(viewPager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = TabsAdapter(activity!!.getSupportFragmentManager())
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }


    /**
     * Add Fragments in TabsAdapter
     */
    private fun setAdapter() {

        val adapter = TabsAdapter(childFragmentManager)
        adapter.addFragment(todayFragment, getString(com.rontaxi.R.string.today_tab_title))
        adapter.addFragment(totalFragment, getString(com.rontaxi.R.string.total_tab_title))
        viewPager.setAdapter(adapter)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    /**
     * Handle Click On Tab
     */
    private fun onClick() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selectedTabPosition = tab.position


                if (selectedTabPosition == 0) {
                    todayFragment.offset = 0
                    todayFragment.totalData = 0
                    todayFragment.dataEarning.clear()
                    todayFragment.driverEarningAdapter.notifyDataSetChanged()


                } else {
                    totalFragment.offset = 0
                    totalFragment.totalData = 0
                    totalFragment.dataEarning.clear()
                    totalFragment.driverEarningAdapter.notifyDataSetChanged()
                }

                callEarning()


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    fun drivingEarningData(data: DrivingEarningData) {
        if (selectedTabPosition == 0) {
            todayFragment.drivingEarningData(data)


        } else if (selectedTabPosition == 1) {
            totalFragment.drivingEarningData(data)
        }
    }


}

