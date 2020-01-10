package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.logout.LogoutLiveData
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.HomeRiderViewModel
import dagger.Module
import dagger.Provides


@Module
class HomeRiderModule {


    @Provides
    fun provideHomeRiderViewModel(context:Context, activity: HomeRiderActivity, socketManager: SocketManager, apiService: ApiService): HomeRiderViewModel {


        val viewModel=ViewModelProviders.of(activity).get(HomeRiderViewModel::class.java)
            viewModel.socketManager=socketManager
            viewModel.logoutLiveData=LogoutLiveData(context,apiService)

        return viewModel
    }

}