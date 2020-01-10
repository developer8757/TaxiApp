package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.rider.profile.RiderProfileFragment
import com.rontaxi.view.home.rider.profile.RiderProfileViewModel
import com.rontaxi.view.home.rider.profile.UpdateProfileLiveData
import dagger.Module
import dagger.Provides


@Module
class ProfileModule {

    @Provides
    fun provideProfileViewModel(fragment: RiderProfileFragment, apiService: ApiService, context: Context): RiderProfileViewModel {

        val viewModel=ViewModelProviders.of(fragment).get(RiderProfileViewModel::class.java)
            viewModel.updateProfileLiveData= UpdateProfileLiveData(apiService, context)

        return viewModel
    }
}