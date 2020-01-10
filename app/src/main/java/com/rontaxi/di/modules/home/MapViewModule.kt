package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import com.rontaxi.view.home.driver.home.map.DriverAvailabilityLiveData
import com.rontaxi.view.home.driver.home.map.MapViewFragment
import com.rontaxi.view.home.driver.home.map.MapViewModel
import com.rontaxi.view.home.rider.godview.GetCancelReasonsLiveData
import dagger.Module
import dagger.Provides


@Module
class MapViewModule {


    @Provides
    fun provideMapViewModel(fragment: MapViewFragment, apiService: ApiService, socketManager: SocketManager, context: Context): MapViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(MapViewModel::class.java)
            viewModel.driverAvailabilityLiveData= DriverAvailabilityLiveData(socketManager, context)
            viewModel.updateBookingStatusLiveData= UpdateBookingStatusLiveData(socketManager)
        viewModel.getCancellationReasonsLiveData= GetCancelReasonsLiveData(apiService)


        return viewModel
    }
}