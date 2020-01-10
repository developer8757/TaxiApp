package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.payment.*
import dagger.Module
import dagger.Provides

@Module
class PaymentModule {
    @Provides
    fun providesPaymentViewModel(
        apiService: ApiService,
        fragment: PaymentFragment,
        context: Context
    ): PaymentViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(PaymentViewModel::class.java)
        viewModel.paymentListLiveData= PaymentListLiveData(apiService,context)
        viewModel.defaultPaymentLiveData= DefaultPaymentLiveData(apiService)
        viewModel.deleteCardLiveData=DeleteCardLiveData(apiService)
        return viewModel
    }
}