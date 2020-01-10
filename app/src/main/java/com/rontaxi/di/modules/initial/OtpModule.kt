package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.initial.driver.otp.DriverOtpFragment
import com.rontaxi.view.initial.driver.otp.DriverOtpViewModel
import com.rontaxi.view.initial.rider.otp.OtpFragment
import com.rontaxi.view.initial.rider.otp.OtpVerificationLiveData
import com.rontaxi.view.initial.rider.otp.OtpViewModel
import com.rontaxi.view.initial.rider.otp.ResendOtpLiveData
import dagger.Module
import dagger.Provides


@Module
class OtpModule {



    @Provides
    fun provideDriverOtpViewModel(fragment: DriverOtpFragment, apiService: ApiService, context: Context, socketManager: SocketManager): DriverOtpViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(DriverOtpViewModel::class.java)
            viewModel.otpVerificationLiveData= OtpVerificationLiveData(apiService, context)
            viewModel.resendOtpLiveData= ResendOtpLiveData(apiService, context)
            viewModel.socketManager=socketManager


        return viewModel

    }


    @Provides
    fun provideOtpViewModel(fragment: OtpFragment, apiService: ApiService, context: Context, socketManager: SocketManager): OtpViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(OtpViewModel::class.java)
        viewModel.otpVerificationLiveData= OtpVerificationLiveData(apiService, context)
        viewModel.resendOtpLiveData= ResendOtpLiveData(apiService, context)
        viewModel.socketManager=socketManager


        return viewModel

    }

}