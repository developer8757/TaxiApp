package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.GetBookingLiveData
import com.rontaxi.view.home.driver.home.UpdateBookingStatusLiveData
import com.rontaxi.view.home.rider.godview.*
import com.rontaxi.view.home.rider.ridehistory.RideHistoryLiveData
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeLiveData
import dagger.Module
import dagger.Provides


@Module
class GodViewModule {

    @Provides
    fun provideGodViewModel(
        fragment: GodViewFragment,
        apiService: ApiService,
        socketManager: SocketManager,
        context: Context
    ): GodViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(GodViewModel::class.java)
        //  viewModel.socketManager=socketManager
        viewModel.getBookingStatus = GetBookingStatusLiveData(socketManager)
        viewModel.vehicleTypeLiveData = VehicleTypeLiveData(apiService, context)
        viewModel.getNearByCarsLiveData = GetNearByCarsLiveData(socketManager)
        viewModel.getRoutesWithFairLiveData = GetRoutesWithFairLiveData(socketManager)
        viewModel.createBookingLiveData = CreateBookingLiveData(socketManager)
        //viewModel.cancelBookingLiveData=CancelBookingLiveData(socketManager)
        viewModel.updateBookingStatusLiveData = UpdateBookingStatusLiveData(socketManager)
        viewModel.createBookingStatusLiveData = CreateBookingStatusLiveData(socketManager, context)
        viewModel.bookingStatusLiveData = BookingStatusLiveData(socketManager)
        viewModel.getCancellationReasonsLiveData = GetCancelReasonsLiveData(apiService)
        viewModel.getSOSLiveData = GetSOSLiveData(socketManager)
        viewModel.getBookingLiveData = GetBookingLiveData(apiService, context)

        viewModel.rideHistoryLiveData = RideHistoryLiveData(apiService, context)


        return viewModel

    }


}