package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import com.rontaxi.view.initial.driver.register.DriverRegisterViewModel
import com.rontaxi.view.initial.driver.register.ValidatePhoneLiveData
import com.rontaxi.view.initial.rider.register.RegisterFragment
import com.rontaxi.view.initial.rider.register.RegisterLiveData
import com.rontaxi.view.initial.rider.register.RegisterViewModel
import dagger.Module
import dagger.Provides


@Module
class RegisterModule {


    @Provides
    fun provideRegisterViewModel(fragment: RegisterFragment, apiService: ApiService, context: Context): RegisterViewModel {

        val viewModel=ViewModelProviders.of(fragment).get(RegisterViewModel::class.java)
            viewModel.registerLiveData= RegisterLiveData(apiService, context)
        return viewModel

    }

    @Provides
    fun provideDriverRegisterViewModel(fragment: DriverRegisterFragment, apiService: ApiService, context: Context): DriverRegisterViewModel {

        val viewModel=ViewModelProviders.of(fragment).get(DriverRegisterViewModel::class.java)
            viewModel.validatePhoneLiveData= ValidatePhoneLiveData(apiService,context)
        return viewModel

    }
}