package com.rontaxi.view.home.rider.ridehistory

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getToken
import com.rontaxi.model.Booking
import com.rontaxi.model.DataRiderHistory
import com.rontaxi.model.RiderHistoryResponseModel
import com.rontaxi.model.drivingearning.DrivingEarningData
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.driver.home.earnings.DriverEarningTodayFragment
import com.rontaxi.view.home.rider.HomeRiderActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_earnings.*
import kotlinx.android.synthetic.main.fragment_rides_container.*
import kotlinx.android.synthetic.main.fragment_rides_container.tabLayout
import kotlinx.android.synthetic.main.fragment_rides_container.viewPager
import kotlinx.android.synthetic.main.fragment_trip_history.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import java.sql.Timestamp
import javax.inject.Inject

class RidesContainerFragment : BaseFragment() {

    @Inject
    lateinit var rideHistoryViewModel: RideHistoryViewModel

    companion object {
        var selectedTabPosition = 0

    }

    val ridesHistoryFragment = RideHistoryFragment()
    val upcomingRidesFragment = UpcomingRidesFragment()

    override fun getLayoutRes() = R.layout.fragment_rides_container

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isLoaded) {
            return
        }

        setupViewPager(viewPager)

        tabLayout!!.setupWithViewPager(viewPager)

        setToolBar()

        onClick()

        getRidesHistory()

        addObserver()


    }

    private fun addObserver() {

        rideHistoryViewModel.rideHistoryLiveData.observe(this, Observer {
            it!!.apply {
                ProgressDialog.hideProgressBar()
                if (it.isSuccessful) {
                    if (it!!.isSuccessful) {
                        rideHistorydata(it.body()?.data!!)
                    } else {
                        tackleError(activity!!, getErrorMessage(it.errorBody()!!))
                    }
                } else
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })

    }

    fun rideHistorydata(data: DataRiderHistory) {
        if (selectedTabPosition == 0) {
            ridesHistoryFragment.drivingEarningData(data)


        } else if (selectedTabPosition == 1) {
            upcomingRidesFragment.drivingEarningData(data)
        }
    }


    /**
     * Handle Click On Tab
     */
    private fun onClick() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selectedTabPosition = tab.position


                if (selectedTabPosition == 0) {
                    ridesHistoryFragment.resetData()
                } else {
                    upcomingRidesFragment.resetData()
                }

                getRidesHistory()


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(ridesHistoryFragment, "PAST RIDES")
        adapter.addFragment(upcomingRidesFragment, "UPCOMING RIDES")
        viewPager.adapter = adapter
    }

    private fun setToolBar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.tripshistory)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llEnd.removeAllViews()
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            if (activity is HomeRiderActivity)
                (activity as HomeRiderActivity).onBackPressed()
            else if (activity is HomeDriverActivity)
                (activity as HomeDriverActivity).onBackPressed()

        }
    }

    fun getRidesHistory() {
        if (ridesHistoryFragment.itemLimit < 1 || upcomingRidesFragment.itemLimit < 1)
            ProgressDialog.showProgressBar(context!!, "", false)
        if (selectedTabPosition == 0) {
            rideHistoryViewModel.rideHistory(0, ridesHistoryFragment.itemLimit)
        } else if (selectedTabPosition == 1) {
            rideHistoryViewModel.rideHistory(1, upcomingRidesFragment.itemLimit)
        }
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}