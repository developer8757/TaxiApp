package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.driver.home.rating.DriverRatingFragment
import com.rontaxi.view.home.driver.home.rating.DriverRatingLiveData
import com.rontaxi.view.home.driver.home.rating.DriverRatingViewModel
import com.rontaxi.view.home.driver.home.rating.adapter.DriverRatingAdapter
import dagger.Module
import dagger.Provides

@Module
class DriverRatingModule {

    @Provides
    fun providesDriverRatingViewModel(fragment: DriverRatingFragment, apiService: ApiService, context: Context): DriverRatingViewModel {
        val viewModel= ViewModelProviders.of(fragment).get(DriverRatingViewModel::class.java)
        viewModel.driverRatingLiveData = DriverRatingLiveData(apiService,context)
        return viewModel
    }

    @Provides
    fun adapter():DriverRatingAdapter
    {
        return  DriverRatingAdapter()
    }


}