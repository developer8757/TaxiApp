package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.addtip.AddTipFragment
import com.rontaxi.view.home.rider.addtip.AddTipLiveData
import com.rontaxi.view.home.rider.addtip.AddTipViewModel
import dagger.Module
import dagger.Provides

@Module
class AddTipModule {
    @Provides
    fun providesAddTipViewModel(
        apiService: ApiService,
        fragment: AddTipFragment
    ): AddTipViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(AddTipViewModel::class.java)
        viewModel.addTipLiveData = AddTipLiveData(apiService)
        return viewModel
    }
}