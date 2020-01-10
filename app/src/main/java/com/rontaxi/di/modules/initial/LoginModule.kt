package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.initial.driver.login.DriverLoginFragment
import com.rontaxi.view.initial.driver.login.DriverLoginViewModel
import com.rontaxi.view.initial.driver.login.DriverLoginLiveData
import com.rontaxi.view.initial.rider.login.LoginFragment
import com.rontaxi.view.initial.rider.login.LoginViewModel
import dagger.Module
import dagger.Provides


@Module
class LoginModule {

    @Provides
    fun provideLoginViewModel(fragment: LoginFragment, apiService: ApiService, context: Context, socketManager: SocketManager): LoginViewModel {

        val viewModel=ViewModelProviders.of(fragment).get(LoginViewModel::class.java)
            viewModel.loginLiveData=DriverLoginLiveData(apiService, context)
             viewModel.socketManager=socketManager
        return viewModel
    }


    @Provides
    fun provideDriverLoginViewModel(fragment: DriverLoginFragment, apiService: ApiService, context: Context, socketManager: SocketManager): DriverLoginViewModel {
        val viewModel=ViewModelProviders.of(fragment).get(DriverLoginViewModel::class.java)
            viewModel.loginLiveData= DriverLoginLiveData(apiService, context)
            viewModel.socketManager=socketManager
        return viewModel
    }
}