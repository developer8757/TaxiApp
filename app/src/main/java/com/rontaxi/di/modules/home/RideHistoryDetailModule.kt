package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.help.HelpReasonsLiveData
import com.rontaxi.view.home.rider.payment.BlockDriverLiveData
import com.rontaxi.view.home.rider.ridehistory.RideHistoryDetailFragment
import com.rontaxi.view.home.rider.ridehistory.RideHistoryDetailViewModel
import dagger.Module
import dagger.Provides

@Module
class RideHistoryDetailModule {

    @Provides

    fun provideRideHistoryViewModel(fragment: RideHistoryDetailFragment,apiService: ApiService):RideHistoryDetailViewModel
    {
        val viewModel=ViewModelProviders.of(fragment).get(RideHistoryDetailViewModel::class.java)
        viewModel.helpReasonsLiveData= HelpReasonsLiveData(apiService)
        viewModel.blockDriverLiveData= BlockDriverLiveData(apiService)
        return viewModel
    }

}