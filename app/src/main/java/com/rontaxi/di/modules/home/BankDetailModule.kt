package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.driver.home.payment.*
import com.rontaxi.view.home.rider.payment.DeleteCardLiveData
import com.rontaxi.view.home.rider.payment.PaymentListLiveData
import dagger.Module
import dagger.Provides


@Module
class BankDetailModule {
    @Provides
    fun providesBankDetailViewModel(context: Context,
        apiService: ApiService,
        fragment: BankDetailFragment
    ): BankDetailViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(BankDetailViewModel::class.java)
        viewModel.deleteCardLiveData = DeleteCardLiveData(apiService)
        viewModel.paymentListLiveData= PaymentListDriverLiveData(apiService, context)
        return viewModel
    }
}