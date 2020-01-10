package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.initial.driver.upload.ValidateDocsLiveData
import com.rontaxi.view.initial.driver.vehicledetails.VehicleDetailsFragment
import com.rontaxi.view.initial.driver.vehicledetails.VehicleDetailsViewModel
import dagger.Module
import dagger.Provides


@Module
class VehicleDetailsModule {


    @Provides
    fun provideVehicleDetails(fragment: VehicleDetailsFragment, apiService: ApiService, context: Context): VehicleDetailsViewModel{

        val viewModel=ViewModelProviders.of(fragment).get(VehicleDetailsViewModel::class.java)
            viewModel.validateDocsLiveData=ValidateDocsLiveData(apiService, context)

        return viewModel

    }

}