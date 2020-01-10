package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.*
import com.rontaxi.view.home.driver.home.map.GetAvailabiltyStatusLiveData
import com.rontaxi.view.home.rider.godview.BookingStatusLiveData
import com.rontaxi.view.home.rider.godview.GetBookingStatusLiveData
import com.rontaxi.view.home.rider.godview.GetCancelReasonsLiveData
import com.rontaxi.view.initial.driver.login.DriverLoginLiveData
import dagger.Module
import dagger.Provides

@Module
class HomeDriverModule {


    @Provides
    fun providesHomeDriverModel(
        fragment: HomeDriverFragment,
        apiService: ApiService,
        socketManager: SocketManager,
        context: Context
    ): HomeDriverViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(HomeDriverViewModel::class.java)

        viewModel.availabilityStatusLiveData = GetAvailabiltyStatusLiveData(socketManager, context)

        viewModel.driverLocationUpdateLiveData =
            DriverLocationUpdateLiveData(socketManager, context)

        viewModel.notifyDriverLiveData = NotifyDriverLiveData(socketManager, context)

        viewModel.acceptRejectRequestLiveData = AcceptRejectLiveData(socketManager)

        viewModel.getBookingStatusLiveData = GetBookingStatusLiveData(socketManager)

        viewModel.bookingStatusUpdateLiveData = BookingStatusLiveData(socketManager)

        viewModel.getBookingLiveData = GetBookingLiveData(apiService, context)





        return viewModel
    }

    /* @Provides
     fun providesRequestDialog(context: Context)=NotifyDriverDialog(context)*/
}