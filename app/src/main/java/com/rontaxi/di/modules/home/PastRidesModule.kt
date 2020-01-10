package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.ridehistory.*
import dagger.Module
import dagger.Provides

@Module
class PastRidesModule {
    @Provides
    fun providesPastRidesViewModel(
        apiService: ApiService,
        fragment: RideHistoryFragment,
        context: Context
    ): PastRidesViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(PastRidesViewModel::class.java)
        return viewModel
    }
}