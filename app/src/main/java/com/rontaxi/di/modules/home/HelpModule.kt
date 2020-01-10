package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.help.*
import dagger.Module
import dagger.Provides


@Module
class HelpModule {

    @Provides
    fun provideHelpViewModel(fragment: HelpFragment, apiService: ApiService): HelpViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(HelpViewModel::class.java)
        viewModel.helpReasonsLiveData = HelpReasonsLiveData(apiService)
        viewModel.submitQueryLiveData = SubmitQueryLiveData(apiService)
        viewModel.raiseIssueLiveData = RaiseIssueLiveData(apiService)
        return viewModel
    }
}