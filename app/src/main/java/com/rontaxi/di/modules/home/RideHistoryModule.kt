package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.ridehistory.RideHistoryFragment
import com.rontaxi.view.home.rider.ridehistory.RideHistoryLiveData
import com.rontaxi.view.home.rider.ridehistory.RideHistoryViewModel
import com.rontaxi.view.home.rider.ridehistory.RidesContainerFragment
import dagger.Module
import dagger.Provides

@Module
class RideHistoryModule {
    @Provides
    fun providesTripHistoryViewModel(
        apiService: ApiService,
        fragment: RidesContainerFragment,
        context: Context
    ): RideHistoryViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(RideHistoryViewModel::class.java)
        viewModel.rideHistoryLiveData = RideHistoryLiveData(apiService, context)
        return viewModel
    }
}