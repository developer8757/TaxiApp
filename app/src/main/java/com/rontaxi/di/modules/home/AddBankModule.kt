package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.driver.home.payment.AddBankFragment
import com.rontaxi.view.home.driver.home.payment.AddBankLiveData
import com.rontaxi.view.home.driver.home.payment.AddBankViewModel
import dagger.Module
import dagger.Provides

@Module
class AddBankModule {
    @Provides
    fun providesAddCardViewModel(
        apiService: ApiService,
        fragment: AddBankFragment
    ): AddBankViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(AddBankViewModel::class.java)
        viewModel.addBankLiveData = AddBankLiveData(apiService)
        return viewModel
    }
}