package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.driver.home.earnings.DriverEarningLiveData
import com.rontaxi.view.home.driver.home.earnings.DriverEarningViewModel
import com.rontaxi.view.home.driver.home.earnings.EarningsFragment
import dagger.Module
import dagger.Provides

@Module
class DriverEarningModule{
    @Provides
    fun providesDrivingEarningViewModel(apiService: ApiService, fragment: EarningsFragment): DriverEarningViewModel {
        val viewModel= ViewModelProviders.of(fragment).get(DriverEarningViewModel::class.java)
        viewModel.driverEarningLiveData= DriverEarningLiveData(apiService)
        return viewModel
    }
}