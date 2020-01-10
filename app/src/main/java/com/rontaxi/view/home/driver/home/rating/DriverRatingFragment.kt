package com.rontaxi.view.home.driver.home.rating

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.rontaxi.base.BaseFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.rontaxi.R
import com.rontaxi.model.driverrating.RatingData
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.home.rating.adapter.DriverRatingAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_driver_rating.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class DriverRatingFragment : BaseFragment() {

    @Inject
    lateinit var driverRatingViewModel: DriverRatingViewModel

    @Inject
    lateinit var driverRatingAdapter: DriverRatingAdapter

    var driverRatingArrayList = ArrayList<RatingData>()

    override fun getLayoutRes() = R.layout.fragment_driver_rating

    override fun showTitleBar() = true

    var itemLimit = 0
    var totalValues = 0
    var isAllLoaded = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        if (isLoaded) {
//            return
//        }
        setAdapter()
        addObserver()
        setToolBar()

        onRecyclerViewScroll()

        //calling getDriverRatingsApi
        getDriverRatingApi(itemLimit)
    }

    private fun setToolBar() {
        toolbarTitle.text = resources.getString(R.string.rating)
    }


    private fun getDriverRatingApi(limit: Int) {
         ProgressDialog.showProgressBar(context!!, "", false)
        driverRatingViewModel.getDriverRatings(limit)
    }


    private fun addObserver() {
        driverRatingViewModel.driverRatingLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()

            if (it!!.isSuccessful) {

                it.body()!!.data.apply {
                    itemLimit = itemLimit.plus(this.limit)
                    totalValues = this.total

                    tvRating.text = this.average.toString()
                    ratingBar.rating = this.average.toFloat()
                    tvTotalRating.text = "${this.total} ${getString(R.string.total)}"
                    pbFiveStar.progress = this.fiveStar
                    pbFourStar.progress = this.fourStar
                    pbThreeStar.progress = this.threeStar
                    pbTwoStar.progress = this.twoStar
                    pbOneStar.progress = this.oneStar

                    if (!this.ratingData.isNullOrEmpty()) {
                        driverRatingArrayList.addAll(this.ratingData)
                        driverRatingAdapter.notifyDataSetChanged()
                        isAllLoaded = true
                    }


                    if(driverRatingArrayList.size==0){
                        tvNoRecordes.visibility=View.VISIBLE
                    }else{
                        tvNoRecordes.visibility=View.GONE
                    }
                }

            } else {
                ProgressDialog.hideProgressBar()
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
                tvNoRecordes.visibility = View.VISIBLE
            }

        })
    }


    private fun onRecyclerViewScroll() {
        rvRatings.addOnScrollListener(onScrollListener)
 
    }


    var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (!recyclerView.canScrollVertically(1)) {

                if (isAllLoaded && itemLimit < totalValues) {
                    isAllLoaded = false
                    getDriverRatingApi(itemLimit)
                }
            }
        }
    }


    private fun setAdapter() {
        driverRatingAdapter.driverRatingArrayList = driverRatingArrayList
        rvRatings.layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        rvRatings.adapter = driverRatingAdapter

    }
}