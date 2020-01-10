package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.profile.AccountFragment
import com.rontaxi.view.home.driver.home.profile.AccountViewModel
import com.rontaxi.view.home.logout.LogoutLiveData
import dagger.Module
import dagger.Provides


@Module
class AccountModule {

    @Provides
    fun providesAccountViewModel(context: Context,fragment: AccountFragment,socketManager : SocketManager, apiService: ApiService): AccountViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(AccountViewModel::class.java)

        viewModel.socketManager=socketManager
        viewModel.logoutLiveData= LogoutLiveData(context,apiService)

        return viewModel


    }
}