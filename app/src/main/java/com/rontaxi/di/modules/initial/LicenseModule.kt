package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.initial.driver.license.LicenseDetailFragment
import com.rontaxi.view.initial.driver.license.LicenseDetailsViewModel
import com.rontaxi.view.initial.driver.upload.ValidateDocsLiveData
import dagger.Module
import dagger.Provides


@Module
class LicenseModule {

    @Provides
    fun provideLicenseViewModel(fragment: LicenseDetailFragment, apiService: ApiService, context: Context): LicenseDetailsViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(LicenseDetailsViewModel::class.java)
            viewModel.validateDocsLiveData= ValidateDocsLiveData(apiService, context)
        return  viewModel
    }
}