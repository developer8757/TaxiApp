package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.driver.home.profile.updatephone.ChangePhoneFragment
import com.rontaxi.view.home.driver.home.profile.updatephone.ChangePhoneLiveData
import com.rontaxi.view.home.driver.home.profile.updatephone.ChangePhoneViewModel
import dagger.Module
import dagger.Provides

@Module
class ChangePhoneModule {

    @Provides
    fun providesUpdateDriverPhoneViewModel(fragment: ChangePhoneFragment, apiService: ApiService):
            ChangePhoneViewModel {
        val viewModel= ViewModelProviders.of(fragment).get(ChangePhoneViewModel::class.java)
        viewModel.UpdateDriverPhoneLiveData = ChangePhoneLiveData(apiService)
        return viewModel


    }
}