package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.rate.RateDriverFragment
import com.rontaxi.view.home.rider.rate.RateDriverViewModel
import com.rontaxi.view.home.rider.rate.RatingLiveData
import dagger.Module
import dagger.Provides


@Module
class RateViewModule {

    @Provides
    fun provideDriverRateViewModel(apiService: ApiService, fragment: RateDriverFragment): RateDriverViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(RateDriverViewModel::class.java)
            viewModel.ratingLiveData= RatingLiveData(apiService)

        return viewModel

    }
}