package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.initial.driver.forgotpassword.DriverForgotPasswordFragment
import com.rontaxi.view.initial.driver.forgotpassword.DriverForgotPasswordViewModel
import com.rontaxi.view.initial.rider.forgotpassword.ForgotPasswordFragment
import com.rontaxi.view.initial.rider.forgotpassword.ForgotPasswordLiveData
import com.rontaxi.view.initial.rider.forgotpassword.ForgotPasswordViewModel
import dagger.Module
import dagger.Provides


@Module
class ForgotModule {

    @Provides
    fun provideForgotViewModel(fragment: ForgotPasswordFragment, apiService: ApiService, context: Context): ForgotPasswordViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(ForgotPasswordViewModel::class.java)
            viewModel.forgotPasswordLiveData= ForgotPasswordLiveData(apiService,context)

        return viewModel

    }

    @Provides
    fun provideDriverForgotViewModel(fragment: DriverForgotPasswordFragment, apiService: ApiService, context: Context): DriverForgotPasswordViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(DriverForgotPasswordViewModel::class.java)
        viewModel.forgotPasswordLiveData= ForgotPasswordLiveData(apiService, context)

        return viewModel

    }
}