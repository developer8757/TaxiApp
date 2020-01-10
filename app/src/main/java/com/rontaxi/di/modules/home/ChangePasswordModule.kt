package com.rontaxi.di.modules.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.home.logout.LogoutLiveData
import com.rontaxi.view.home.rider.changepassword.ChangePasswordFragment
import com.rontaxi.view.home.rider.changepassword.ChangePasswordLiveData
import com.rontaxi.view.home.rider.changepassword.ChangePasswordViewModel
import dagger.Module
import dagger.Provides

@Module
class ChangePasswordModule {
    @Provides
    fun providesChangePasswordViewModel(
        apiService: ApiService,
        fragment: ChangePasswordFragment,
        context: Context
    ): ChangePasswordViewModel {
        val viewModel = ViewModelProviders.of(fragment).get(ChangePasswordViewModel::class.java)
        viewModel.changePasswordLiveData = ChangePasswordLiveData(apiService, context)
        viewModel.logoutLiveData = LogoutLiveData(context,apiService)
        return viewModel
    }


}