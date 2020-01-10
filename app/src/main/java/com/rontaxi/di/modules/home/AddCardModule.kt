package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.payment.*
import dagger.Module
import dagger.Provides

@Module
class AddCardModule {
    @Provides
    fun providesAddCardViewModel(
        apiService: ApiService,
        fragment: AddCardFragment
    ): AddCardViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(AddCardViewModel::class.java)
        viewModel.paymentLiveData = PaymentLiveData(apiService)
               return viewModel
    }
}