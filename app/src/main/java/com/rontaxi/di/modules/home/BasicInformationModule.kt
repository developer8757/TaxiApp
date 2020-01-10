package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.driver.home.profile.basicinfo.BasicInformationFragment
import com.rontaxi.view.home.driver.home.profile.basicinfo.BasicInformationViewModel
import com.rontaxi.view.home.driver.home.profile.basicinfo.UpdateDriverProfileLiveData
import dagger.Module
import dagger.Provides

@Module
class BasicInformationModule {

    @Provides
    fun providesBasicInfoViewModel(fragment: BasicInformationFragment, apiService: ApiService, context: Context) : BasicInformationViewModel{

        val viewModel = ViewModelProviders.of(fragment).get(BasicInformationViewModel::class.java)
        viewModel.updateDriverProfileLiveData= UpdateDriverProfileLiveData(apiService,context)

        return viewModel
    }
}