package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.driver.home.earnings.DriverEarningLiveData
import com.rontaxi.view.home.driver.home.earnings.DriverEarningTodayFragment
import com.rontaxi.view.home.driver.home.earnings.DriverEarningTodayViewModel
import dagger.Module
import dagger.Provides

@Module
class DriverEarningTodayModule {
    @Provides
    fun providesDrivingEarningTodayViewModel(apiService: ApiService, fragment:DriverEarningTodayFragment):DriverEarningTodayViewModel{
        val viewModel=ViewModelProviders.of(fragment).get(DriverEarningTodayViewModel::class.java)
        return  viewModel
    }

}
