package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.GetBookingLiveData
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import com.rontaxi.view.home.rider.godview.*
import com.rontaxi.view.home.rider.ridehistory.RideHistoryLiveData
import com.rontaxi.view.home.rider.schedulebooking.ScheduleBookingFragment
import com.rontaxi.view.home.rider.schedulebooking.ScheduleBookingLiveData
import com.rontaxi.view.home.rider.schedulebooking.ScheduleBookingViewModel
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeLiveData
import dagger.Module
import dagger.Provides


@Module
class ScheduleBookingViewModule {

    @Provides
    fun provideScheduleBookingViewModel(
        fragment: ScheduleBookingFragment,
        socketManager: SocketManager,
        apiService: ApiService,
        context: Context
    ): ScheduleBookingViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(ScheduleBookingViewModel::class.java)

        viewModel.getRoutesWithFairLiveData = GetRoutesWithFairLiveData(socketManager)
        viewModel.scheduleBookingLiveData = ScheduleBookingLiveData(apiService, context)


        return viewModel

    }


}