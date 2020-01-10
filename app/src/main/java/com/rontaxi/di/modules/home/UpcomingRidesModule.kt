package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import com.rontaxi.view.home.rider.godview.GetCancelReasonsLiveData
import com.rontaxi.view.home.rider.ridehistory.*
import dagger.Module
import dagger.Provides

@Module
class UpcomingRidesModule {
    @Provides
    fun providesTripHistoryViewModel(
        apiService: ApiService,
        fragment: UpcomingRidesFragment,
        socketManager: SocketManager,
        context: Context
    ): UpcomingRidesViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(UpcomingRidesViewModel::class.java)
        viewModel.updateBookingStatusLiveData = UpdateBookingStatusLiveData(socketManager)
        viewModel.getCancellationReasonsLiveData = GetCancelReasonsLiveData(apiService)

        return viewModel
    }
}