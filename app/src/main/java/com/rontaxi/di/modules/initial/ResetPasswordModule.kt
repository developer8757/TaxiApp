package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.initial.driver.resetpassword.DriverResetPasswordFragment
import com.rontaxi.view.initial.driver.resetpassword.DriverResetPasswordViewModel
import com.rontaxi.view.initial.rider.resetpassword.ResetPasswordFragment
import com.rontaxi.view.initial.rider.resetpassword.ResetPasswordLiveData
import com.rontaxi.view.initial.rider.resetpassword.ResetPasswordViewModel
import dagger.Module
import dagger.Provides


@Module
class ResetPasswordModule {


    @Provides
    fun provideDriverResetPasswordViewModel(fragment: DriverResetPasswordFragment, apiService: ApiService, context: Context): DriverResetPasswordViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(DriverResetPasswordViewModel::class.java)
            viewModel.resetPasswordLiveData= ResetPasswordLiveData(apiService, context)
        return viewModel
    }



    @Provides
    fun provideResetPasswordViewModel(fragment: ResetPasswordFragment, apiService: ApiService, context: Context): ResetPasswordViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(ResetPasswordViewModel::class.java)
        viewModel.resetPasswordLiveData= ResetPasswordLiveData(apiService, context)
        return viewModel
    }
}